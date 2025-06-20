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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
                // Validate and create journal header
                CreateJournalHeaderRequest headerRequest = request.getJournalHeader();
                Branch branch = branchRepository.findById(headerRequest.getBranchId())
                        .orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + headerRequest.getBranchId()));

                Currency headerCurrency = currencyRepository.findById(headerRequest.getCurrencyId())
                        .orElseThrow(() -> new EntityNotFoundException("Currency not found with id: " + headerRequest.getCurrencyId()));

                // Calculate total debit and credit from items
                BigDecimal totalDebit = request.getJournalItems().stream()
                        .map(CreateJournalItemRequest::getDebit)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalCredit = request.getJournalItems().stream()
                        .map(CreateJournalItemRequest::getCredit)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                // Validate debit equals credit (double-entry accounting principle)
                if (totalDebit.compareTo(totalCredit) != 0) {
                        throw new IllegalArgumentException("Total debit must equal total credit");
                }

                // Create journal header
                JournalHeader journalHeader = JournalHeader.builder()
                        .branch(branch)
                        .date(headerRequest.getDate())
                        .debit(totalDebit)
                        .credit(totalCredit)
                        .currency(headerCurrency)
                        .currencyValue(headerRequest.getCurrencyValue())
                        .parentType(headerRequest.getParentType())
                       // .parentId(headerRequest.getParentId())
                        .isPosted(headerRequest.getIsPosted())
                        .build();

                // Save header first to generate ID
                journalHeader = journalHeaderRepository.save(journalHeader);

                // Create and save journal items
                JournalHeader finalJournalHeader = journalHeader;
                List<JournalItem> journalItems = request.getJournalItems().stream()
                        .map(itemRequest -> {
                                Account account = accountRepository.findById(itemRequest.getAccountId())
                                        .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + itemRequest.getAccountId()));

                                // Use item currency if specified, otherwise use header currency
                                Currency itemCurrency = itemRequest.getCurrencyId() != null ?
                                        currencyRepository.findById(itemRequest.getCurrencyId())
                                                .orElseThrow(() -> new EntityNotFoundException("Currency not found with id: " + itemRequest.getCurrencyId())) :
                                        headerCurrency;

                                // Use item currency value if specified, otherwise use header currency value
                                BigDecimal itemCurrencyValue = itemRequest.getCurrencyValue() != null ?
                                        itemRequest.getCurrencyValue() :
                                        headerRequest.getCurrencyValue();

                                return JournalItem.builder()
                                        .jornalHeader(finalJournalHeader)
                                        .account(account)
                                        .debit(itemRequest.getDebit())
                                        .credit(itemRequest.getCredit())
                                        .currency(itemCurrency)
                                        .currencyValue(itemCurrencyValue)
                                        .date(itemRequest.getDate() != null ? itemRequest.getDate() : headerRequest.getDate())
                                        .build();
                        })
                        .collect(Collectors.toList());

                // Save all items
                journalItemRepository.saveAll(journalItems);
                journalHeader.setJournalItems(journalItems);

                return mapToJournalResponse(journalHeader);
        }

        @Override
        public List<JournalHeaderResponse> getAllJournals() {
                return journalHeaderRepository.findAll().stream()
                        .map(this::mapToJournalHeaderResponse)
                        .collect(Collectors.toList());
        }

        @Override
        public List<JournalHeaderResponse> getJournalsByDateRange(LocalDate startDate, LocalDate endDate) {
                LocalDateTime startDateTime = startDate.atStartOfDay();
                LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

                return journalHeaderRepository.findByDateBetween(startDateTime, endDateTime).stream()
                        .map(this::mapToJournalHeaderResponse)
                        .collect(Collectors.toList());
        }

        @Override
        public JournalResponse getJournalById(Integer id) {
                JournalHeader journalHeader = journalHeaderRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Journal not found with id: " + id));

                return mapToJournalResponse(journalHeader);
        }

        @Override
        public JournalResponse updateJournal(Integer id, UpdateJournalRequest request) {
                JournalHeader journalHeader = journalHeaderRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Journal not found with id: " + id));

                // Update header if present in request
                if (request.getJournalHeader() != null) {
                        updateJournalHeader(journalHeader, request.getJournalHeader());
                }

                // Update items if present in request
                if (request.getJournalItems() != null && !request.getJournalItems().isEmpty()) {
                        updateJournalItems(journalHeader, request.getJournalItems());
                }

                // Recalculate totals if items were updated
                if (request.getJournalItems() != null && !request.getJournalItems().isEmpty()) {
                        recalculateTotals(journalHeader);
                }

                // Save the updated journal
                journalHeader = journalHeaderRepository.save(journalHeader);

                return mapToJournalResponse(journalHeader);
        }

        private void updateJournalHeader(JournalHeader journalHeader, UpdateJournalHeaderRequest headerRequest) {
                if (headerRequest.getBranchId() != null) {
                        Branch branch = branchRepository.findById(headerRequest.getBranchId())
                                .orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + headerRequest.getBranchId()));
                        journalHeader.setBranch(branch);
                }

                if (headerRequest.getDate() != null) {
                        journalHeader.setDate(headerRequest.getDate());
                }

                if (headerRequest.getCurrencyId() != null) {
                        Currency currency = currencyRepository.findById(headerRequest.getCurrencyId())
                                .orElseThrow(() -> new EntityNotFoundException("Currency not found with id: " + headerRequest.getCurrencyId()));
                        journalHeader.setCurrency(currency);
                }

                if (headerRequest.getCurrencyValue() != null) {
                        journalHeader.setCurrencyValue(headerRequest.getCurrencyValue());
                }

                if (headerRequest.getParentType() != null) {
                        journalHeader.setParentType(headerRequest.getParentType());
                }

//                if (headerRequest.getParentId() != null) {
//                        journalHeader.setParentId(headerRequest.getParentId());
//                }

                if (headerRequest.getIsPosted() != null) {
                        journalHeader.setIsPosted(headerRequest.getIsPosted());
                }

//                if (headerRequest.getPostDate() != null) {
//                        journalHeader.setPostDate(headerRequest.getPostDate());
//                }

//                if (headerRequest.getNotes() != null) {
//                        journalHeader.setNotes(headerRequest.getNotes());
//                }

                // Debit and credit are updated when items are updated
        }

        private void updateJournalItems(JournalHeader journalHeader, List<UpdateJournalItemRequest> itemRequests) {
                // First remove all existing items
                journalItemRepository.deleteAll(journalHeader.getJournalItems());
                journalHeader.getJournalItems().clear();

                // Get header currency and values
                Currency headerCurrency = journalHeader.getCurrency();
                BigDecimal headerCurrencyValue = journalHeader.getCurrencyValue();
                LocalDateTime headerDate = journalHeader.getDate();

                // Then add all new items
                List<JournalItem> newItems = itemRequests.stream()
                        .map(itemRequest -> {
                                Account account = accountRepository.findById(itemRequest.getAccountId())
                                        .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + itemRequest.getAccountId()));

                                // Use item currency if specified, otherwise use header currency
                                Currency currency = itemRequest.getCurrencyId() != null ?
                                        currencyRepository.findById(itemRequest.getCurrencyId())
                                                .orElseThrow(() -> new EntityNotFoundException("Currency not found with id: " + itemRequest.getCurrencyId())) :
                                        headerCurrency;

                                // Use item currency value if specified, otherwise use header currency value
                                BigDecimal currencyValue = itemRequest.getCurrencyValue() != null ?
                                        itemRequest.getCurrencyValue() :
                                        headerCurrencyValue;

                                return JournalItem.builder()
                                        .jornalHeader(journalHeader)
                                        .account(account)
                                        .debit(itemRequest.getDebit())
                                        .credit(itemRequest.getCredit())
                                        .currency(currency)
                                        .currencyValue(currencyValue)
                                        .date(itemRequest.getDate() != null ? itemRequest.getDate() : headerDate)
                                      //  .notes(itemRequest.getNotes())
                                        .build();
                        })
                        .collect(Collectors.toList());

                journalItemRepository.saveAll(newItems);
                journalHeader.setJournalItems(newItems);
        }

        private void recalculateTotals(JournalHeader journalHeader) {
                BigDecimal totalDebit = journalHeader.getJournalItems().stream()
                        .map(JournalItem::getDebit)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalCredit = journalHeader.getJournalItems().stream()
                        .map(JournalItem::getCredit)
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
                LocalDateTime startDateTime = startDate.atStartOfDay();
                LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

                // Get account details
                Account account = accountRepository.findById(accountId)
                        .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + accountId));

                // Calculate opening balance (sum of all entries before start date)
                BigDecimal openingDebit = journalItemRepository.calculateDebitBeforeDate(
                        accountId, startDateTime);
                BigDecimal openingCredit = journalItemRepository.calculateCreditBeforeDate(
                        accountId, startDateTime);
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


        // ... (keep the existing mapping methods)
        private JournalResponse mapToJournalResponse(JournalHeader journalHeader) {
                return JournalResponse.builder()
                        .createJournalHeaderResponse(mapToJournalHeaderResponse(journalHeader))
                        .createJournalItemResponse(journalHeader.getJournalItems().stream()
                                .map(this::mapToJournalItemResponse)
                                .collect(Collectors.toList()))
                        .build();
        }

        private JournalHeaderResponse mapToJournalHeaderResponse(JournalHeader journalHeader) {
                return JournalHeaderResponse.builder()
                        .id(journalHeader.getId())
                        .branch(journalHeader.getBranch())
                        .date(journalHeader.getDate())
                        .debit(journalHeader.getDebit())
                        .credit(journalHeader.getCredit())
                        .currency(journalHeader.getCurrency())
                        .currencyValue(journalHeader.getCurrencyValue())
                        .parentType(journalHeader.getParentType())
                       // .parentId(journalHeader.getParentId())
                        .isPosted(journalHeader.getIsPosted())
                       // .postDate(journalHeader.getPostDate())
                       // .notes(journalHeader.getNotes())
                        .build();
        }

        private JournalItemResponse mapToJournalItemResponse(JournalItem journalItem) {
                return JournalItemResponse.builder()
                        .id(journalItem.getId())
                        .jornalHeader(journalItem.getJornalHeader().getId())
                        .accountId(journalItem.getAccount().getId())
                        .accountName(journalItem.getAccount().getName())
                        .debit(journalItem.getDebit())
                        .credit(journalItem.getCredit())
                        .currency(journalItem.getCurrency())
                        .currencyValue(journalItem.getCurrencyValue())
                        .date(journalItem.getDate())
                       // .notes(journalItem.getNotes())
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