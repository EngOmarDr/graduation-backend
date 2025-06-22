package com.graduationProject._thYear.Journal.services;

import com.graduationProject._thYear.Account.models.Account;
import com.graduationProject._thYear.Branch.models.Branch;
import com.graduationProject._thYear.Currency.models.Currency;
import com.graduationProject._thYear.Journal.dtos.request.*;
import com.graduationProject._thYear.Journal.dtos.response.*;
import com.graduationProject._thYear.Journal.models.JournalHeader;
import com.graduationProject._thYear.Journal.models.JournalItem;
import com.graduationProject._thYear.Journal.repositories.JournalHeaderRepository;
import com.graduationProject._thYear.Journal.repositories.JournalItemRepository;
import com.graduationProject._thYear.Account.repositories.AccountRepository;
import com.graduationProject._thYear.Branch.repositories.BranchRepository;
import com.graduationProject._thYear.Currency.repositories.CurrencyRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class JournalServiceImpl implements JournalService {

        private final JournalHeaderRepository journalHeaderRepository;
        private final JournalItemRepository journalItemRepository;
        private final BranchRepository branchRepository;
        private final CurrencyRepository currencyRepository;
        private final AccountRepository accountRepository;

        @Override
        @Transactional
        public JournalResponse createJournal(CreateJournalRequest request) {
                // Validate branch
                Branch branch = branchRepository.findById(request.getBranchId())
                        .orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + request.getBranchId()));

                // Validate currency
                Currency currency = currencyRepository.findById(request.getCurrencyId())
                        .orElseThrow(() -> new EntityNotFoundException("Currency not found with id: " + request.getCurrencyId()));

                // Create journal items with currency conversion
                List<JournalItem> journalItems = request.getJournalItems().stream()
                        .map(itemRequest -> {
                                Account account = accountRepository.findById(itemRequest.getAccountId())
                                        .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + itemRequest.getAccountId()));

                                // Determine currency to use (item currency has priority)
                                Currency itemCurrency = itemRequest.getCurrencyId() != null ?
                                        currencyRepository.findById(itemRequest.getCurrencyId())
                                                .orElseThrow(() -> new EntityNotFoundException("Currency not found with id: " + itemRequest.getCurrencyId())) :
                                        currency;

                                BigDecimal currencyValue = itemRequest.getCurrencyValue() != null ?
                                        itemRequest.getCurrencyValue() :
                                        request.getCurrencyValue();

                                // Calculate converted amounts
                                BigDecimal debit = itemRequest.getDebit().multiply(currencyValue);
                                BigDecimal credit = itemRequest.getCredit().multiply(currencyValue);

                                return JournalItem.builder()
                                        .journalHeader(null) // Will be set after header is created
                                        .account(account)
                                        .debit(debit)
                                        .credit(credit)
                                        .currency(itemCurrency)
                                        .currencyValue(currencyValue)
                                        .date(itemRequest.getDate() != null ? itemRequest.getDate() : request.getDate())
                                        .build();
                        })
                        .collect(Collectors.toList());

                // Calculate totals with converted amounts
                BigDecimal totalDebit = journalItems.stream()
                        .map(JournalItem::getDebit)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalCredit = journalItems.stream()
                        .map(JournalItem::getCredit)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                // Validate debit equals credit
                if (totalDebit.compareTo(totalCredit) != 0) {
                        throw new IllegalArgumentException("Total debit must equal total credit");
                }

                // Create journal header
                JournalHeader journalHeader = JournalHeader.builder()
                        .branch(branch)
                        .date(request.getDate())
                        .debit(totalDebit)
                        .credit(totalCredit)
                        .currency(currency)
                        .currencyValue(request.getCurrencyValue())
                        .parentType(request.getParentType())
                        .isPosted(request.getIsPosted())
                        .build();

                // Save header first to generate ID
                journalHeader = journalHeaderRepository.save(journalHeader);

                // Set journal header reference for all items
                JournalHeader finalJournalHeader = journalHeader;
                journalItems.forEach(item -> item.setJournalHeader(finalJournalHeader));

                // Save all items
                journalItemRepository.saveAll(journalItems);
                journalHeader.setJournalItems(journalItems);

                return mapToJournalResponse(journalHeader);
        }


        @Override
        public List<JournalResponse> getAllJournals() {
                return journalHeaderRepository.findAll().stream()
                        .map(this::mapToJournalResponse)
                        .collect(Collectors.toList());
        }

        @Override
        public List<JournalResponse> getJournalsByDateRange(LocalDate startDate, LocalDate endDate) {
                LocalDateTime startDateTime = startDate.atStartOfDay();
                LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

                return journalHeaderRepository.findByDateBetween(startDateTime, endDateTime).stream()
                        .map(this::mapToJournalResponse)
                        .collect(Collectors.toList());
        }

        @Override
        public JournalResponse getJournalById(Integer id) {
                JournalHeader journalHeader = journalHeaderRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Journal not found with id: " + id));

                return mapToJournalResponse(journalHeader);
        }

        @Override
        @Transactional
        public JournalResponse updateJournal(Integer id, UpdateJournalRequest request) {
                JournalHeader journalHeader = journalHeaderRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Journal not found with id: " + id));

                // Prevent modification of posted journals
                if (journalHeader.getIsPosted()) {
                        throw new IllegalStateException("Cannot modify a posted journal");
                }

                // Update header fields from request
                updateJournalHeader(journalHeader, request);

                // Update items if present in request
                if (request.getJournalItems() != null && !request.getJournalItems().isEmpty()) {
                        updateJournalItems(journalHeader, request.getJournalItems());
                        recalculateTotals(journalHeader);
                }

                // Save the updated journal
                journalHeader = journalHeaderRepository.save(journalHeader);
                return mapToJournalResponse(journalHeader);
        }

        @Transactional
        private void updateJournalHeader(JournalHeader journalHeader, UpdateJournalRequest request) {
                // Validation - prevent modification of posted journals
                if (!journalHeader.getJournalItems().isEmpty() && journalHeader.getIsPosted()) {
                        throw new IllegalStateException("Cannot modify items of a posted journal");
                }

                if (request.getBranchId() != null) {
                        Branch branch = branchRepository.findById(request.getBranchId())
                                .orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + request.getBranchId()));
                        journalHeader.setBranch(branch);
                }

                if (request.getDate() != null) {
                        journalHeader.setDate(request.getDate());
                }

                if (request.getCurrencyId() != null) {
                        Currency currency = currencyRepository.findById(request.getCurrencyId())
                                .orElseThrow(() -> new EntityNotFoundException("Currency not found with id: " + request.getCurrencyId()));
                        journalHeader.setCurrency(currency);
                }

                if (request.getCurrencyValue() != null) {
                        journalHeader.setCurrencyValue(request.getCurrencyValue());
                }

                if (request.getParentType() != null) {
                        journalHeader.setParentType(request.getParentType());
                }

//                if (request.getParentId() != null) {
//                        journalHeader.setParentId(request.getParentId());
//                }

                if (request.getIsPosted() != null) {
                        journalHeader.setIsPosted(request.getIsPosted());
                }
        }
        @Transactional
        private void updateJournalItems(JournalHeader journalHeader, @NotNull(message = "Journal items are required") List<CreateJournalItemRequest> itemRequests) {
                // Get header currency and values
                Currency headerCurrency = journalHeader.getCurrency();
                BigDecimal headerCurrencyValue = journalHeader.getCurrencyValue();
                LocalDateTime headerDate = journalHeader.getDate();

                // Clear existing items through the managed collection
                List<JournalItem> existingItems = new ArrayList<>(journalHeader.getJournalItems());
                for (JournalItem item : existingItems) {
                        journalHeader.getJournalItems().remove(item);
                        journalItemRepository.delete(item);
                }

                // Add new items
                List<JournalItem> newItems = itemRequests.stream()
                        .map(itemRequest -> {
                                Account account = accountRepository.findById(itemRequest.getAccountId())
                                        .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + itemRequest.getAccountId()));

                                Currency currency = itemRequest.getCurrencyId() != null ?
                                        currencyRepository.findById(itemRequest.getCurrencyId())
                                                .orElseThrow(() -> new EntityNotFoundException("Currency not found with id: " + itemRequest.getCurrencyId())) :
                                        headerCurrency;

                                BigDecimal currencyValue = itemRequest.getCurrencyValue() != null ?
                                        itemRequest.getCurrencyValue() :
                                        headerCurrencyValue;

                                JournalItem newItem = JournalItem.builder()
                                        .journalHeader(journalHeader)
                                        .account(account)
                                        .debit(itemRequest.getDebit())
                                        .credit(itemRequest.getCredit())
                                        .currency(currency)
                                        .currencyValue(currencyValue)
                                        .date(itemRequest.getDate() != null ? itemRequest.getDate() : headerDate)
                                        .build();

                                journalHeader.getJournalItems().add(newItem);
                                return newItem;
                        })
                        .collect(Collectors.toList());

                // Save all new items
                journalItemRepository.saveAll(newItems);
        }
        private void recalculateTotals(JournalHeader journalHeader) {
                BigDecimal totalDebit = journalHeader.getJournalItems().stream()
                        .map(item -> {
                                // Use item's currency value if available, otherwise use header's
                                BigDecimal rate = item.getCurrencyValue() != null ?
                                        item.getCurrencyValue() :
                                        journalHeader.getCurrencyValue();
                                return item.getDebit().multiply(rate);
                        })
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalCredit = journalHeader.getJournalItems().stream()
                        .map(item -> {
                                // Use item's currency value if available, otherwise use header's
                                BigDecimal rate = item.getCurrencyValue() != null ?
                                        item.getCurrencyValue() :
                                        journalHeader.getCurrencyValue();
                                return item.getCredit().multiply(rate);
                        })
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                // Validate debit equals credit
                if (totalDebit.compareTo(totalCredit) != 0) {
                        throw new IllegalArgumentException("Total debit must equal total credit");
                }

                journalHeader.setDebit(totalDebit);
                journalHeader.setCredit(totalCredit);
        }

        @Override
        @Transactional
        public void deleteJournal(Integer id) {
                JournalHeader journalHeader = journalHeaderRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Journal not found with id: " + id));

                journalItemRepository.deleteAll(journalHeader.getJournalItems());
                journalHeaderRepository.delete(journalHeader);
        }

        @Override
        public LedgerReport generateLedgerReport(Integer accountId, LocalDate startDate, LocalDate endDate) {

                if (startDate.isAfter(endDate)) {
                        throw new IllegalArgumentException("Start date must be before end date");
                }
                LocalDateTime startDateTime = startDate.atStartOfDay();
                LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

                Account account = accountRepository.findById(accountId)
                        .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + accountId));

                // Calculate opening balance (sum of all entries before start date)
                BigDecimal openingDebit = journalItemRepository.calculateDebitBeforeDate(accountId, startDateTime);
                BigDecimal openingCredit = journalItemRepository.calculateCreditBeforeDate(accountId, startDateTime);
                BigDecimal openingBalance = openingDebit.subtract(openingCredit);

                // Get entries for the period
                List<JournalItem> entries = journalItemRepository.findEntriesByAccountAndDateRange(
                        accountId, startDateTime, endDateTime);

                // Calculate period totals
                BigDecimal totalDebit = journalItemRepository.calculateDebitBetweenDates(
                        accountId, startDateTime, endDateTime);
                BigDecimal totalCredit = journalItemRepository.calculateCreditBetweenDates(
                        accountId, startDateTime, endDateTime);

                // Calculate closing balance
                BigDecimal closingBalance = openingBalance.add(totalDebit).subtract(totalCredit);

                return LedgerReport.builder()
                        .accountId(accountId)
                        .accountCode(account.getCode())
                        .accountName(account.getName())
                        .startDate(startDateTime)
                        .endDate(endDateTime)
                        .openingBalance(openingBalance)
                        .totalDebit(totalDebit)
                        .totalCredit(totalCredit)
                        .closingBalance(closingBalance)
                        .entries(entries.stream()
                                .map(this::mapToLedgerEntry)
                                .collect(Collectors.toList()))
                        .build();
        }


        private JournalResponse mapToJournalResponse(JournalHeader journalHeader) {
                return JournalResponse.builder()
                        .id(journalHeader.getId())
                        .branchId(journalHeader.getBranch().getId())
                        .date(journalHeader.getDate())
                        .totalDebit(journalHeader.getDebit())
                        .totalCredit(journalHeader.getCredit())
                        .currencyId(journalHeader.getCurrency().getId())
                        .currencyValue(journalHeader.getCurrencyValue())
                        .isPosted(journalHeader.getIsPosted())
                        .parentType(journalHeader.getParentType())
                 //       .parentId(journalHeader.getParentId())  // Uncomment if using parentId
                        .journalItems(journalHeader.getJournalItems().stream()
                                .map(this::mapToJournalItemResponse)
                                .collect(Collectors.toList()))
                        .build();
        }

        private JournalItemResponse mapToJournalItemResponse(JournalItem journalItem) {
                return JournalItemResponse.builder()
                        .id(journalItem.getId())
                        .accountId(journalItem.getAccount().getId())
                        .debit(journalItem.getDebit())
                        .credit(journalItem.getCredit())
                        .currencyId(journalItem.getCurrency().getId())
                        .currencyValue(journalItem.getCurrencyValue())
                        .date(journalItem.getDate())
                        .build();
        }

        private LedgerReport.LedgerEntry mapToLedgerEntry(JournalItem journalItem) {
                return LedgerReport.LedgerEntry.builder()
                        .id(journalItem.getId())
                        .date(journalItem.getDate())
                        .debit(journalItem.getDebit())
                        .credit(journalItem.getCredit())
                       // .notes(journalItem.getNotes())
                        .build();
        }
}