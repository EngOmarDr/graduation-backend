package com.graduationProject._thYear.Journal.services;

import com.graduationProject._thYear.Account.models.Account;
import com.graduationProject._thYear.Currency.models.Currency;
import com.graduationProject._thYear.Journal.dtos.request.*;
import com.graduationProject._thYear.Journal.dtos.response.*;
import com.graduationProject._thYear.Journal.models.JournalHeader;
import com.graduationProject._thYear.Journal.models.JournalItem;
import com.graduationProject._thYear.Journal.models.JournalKind;
import com.graduationProject._thYear.Journal.repositories.JournalHeaderRepository;
import com.graduationProject._thYear.Journal.repositories.JournalItemRepository;
import com.graduationProject._thYear.Account.repositories.AccountRepository;
import com.graduationProject._thYear.Branch.models.Branch;
import com.graduationProject._thYear.Branch.repositories.BranchRepository;
import com.graduationProject._thYear.Currency.repositories.CurrencyRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Tuple;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
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

                // Validate currency (mandatory in header)
                Currency currency = currencyRepository.findById(request.getCurrencyId())
                        .orElseThrow(() -> new EntityNotFoundException("Currency not found with id: " + request.getCurrencyId()));

                // Create journal items (store original values)
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

                                return JournalItem.builder()
                                        .journalHeader(null) // Will be set after header is created
                                        .account(account)
                                        .debit(itemRequest.getDebit()) // Store original value
                                        .credit(itemRequest.getCredit()) // Store original value
                                        .currency(itemCurrency)
                                        .currencyValue(currencyValue)
                                        .date(itemRequest.getDate() != null ? itemRequest.getDate() : request.getDate())
                                        .notes(itemRequest.getNotes())
                                        .build();
                        })
                        .collect(Collectors.toList());

                // Calculate totals with currency conversion
                BigDecimal totalDebit = journalItems.stream()
                        .map(item -> {
                                BigDecimal rate = item.getCurrencyValue() != null ?
                                        item.getCurrencyValue() :
                                        request.getCurrencyValue();
                                return item.getDebit().multiply(rate);
                        })
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalCredit = journalItems.stream()
                        .map(item -> {
                                BigDecimal rate = item.getCurrencyValue() != null ?
                                        item.getCurrencyValue() :
                                        request.getCurrencyValue();
                                return item.getCredit().multiply(rate);
                        })
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
                        .kind(JournalKind.fromCode(request.getKind()))
                //        .parentId(request.getParentId())
                        .isPosted(request.getIsPosted())
                        .build();

                // Save header first to generate ID
                journalHeader = journalHeaderRepository.save(journalHeader);

                // Set journal header reference for all items
                JournalHeader finalJournalHeader = journalHeader;
                journalItems.forEach(item -> item.setJournalHeader(finalJournalHeader));

                // Save all items (with original values)
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
                        journalHeader.setBranch(branch);;
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

                if (request.getKind() != null) {
                        journalHeader.setKind(JournalKind.fromCode(request.getKind()));
                }

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
                                        .notes(itemRequest.getNotes())
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

        public List<JournalResponse> searchJournalsByParentType(Byte parentType) {
                List<JournalHeader> journals;

                if (parentType != null) {
                        journals = journalHeaderRepository.findByParentType(parentType);
                } else {
                        journals = journalHeaderRepository.findAll();
                }

                return journals.stream()
                        .map(this::mapToJournalResponse)
                        .collect(Collectors.toList());
        }

        @Override
        public LedgerReport generateLedgerReport(Integer accountId, Integer branchId, LocalDate startDate, LocalDate endDate) {
        
                if (startDate.isAfter(endDate)) {
                        throw new IllegalArgumentException("Start date must be before end date");
                }
                LocalDateTime startDateTime = startDate.atStartOfDay();
                LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

                Account account = accountRepository.findById(accountId)
                        .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + accountId));

                List<Integer> children = account.getAllChildren(new LinkedList<>());

                // Calculate opening balance (sum of all entries before start date)
                BigDecimal openingDebit = journalItemRepository.calculateDebitBeforeDate(branchId, children, startDateTime);
                BigDecimal openingCredit = journalItemRepository.calculateCreditBeforeDate(branchId, children, startDateTime);
                BigDecimal openingBalance = openingDebit.subtract(openingCredit);

                // Get entries for the period
                List<JournalItem> entries = journalItemRepository.findEntriesByAccountAndDateRange(
                        children, branchId, startDateTime, endDateTime);

                // Calculate period totals
                BigDecimal totalDebit = journalItemRepository.calculateDebitBetweenDates(
                        children, branchId,  startDateTime, endDateTime);
                BigDecimal totalCredit = journalItemRepository.calculateCreditBetweenDates(
                        children, branchId, startDateTime, endDateTime);

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

        @Override
        public TrialBalanceReportResponse generateTrialBalanceReport(Integer branchId, LocalDate startDate, LocalDate endDate) {

                LocalDateTime startDateTime = startDate == null ? endDate.withDayOfYear(1).atStartOfDay() : startDate.plusDays(1).atStartOfDay();
                LocalDateTime endDateTime = endDate.atStartOfDay();
                Tuple totals = journalItemRepository.getTotalDebitAndCreditWithinTimeRange(branchId, startDateTime, endDateTime);
                List<Tuple> entries = journalItemRepository.getTotalDebitAndCreditByAccount(branchId, startDateTime, endDateTime);
                List<TrialBalanceReportResponse.BalanceEntry> items =  entries.stream()
                        .map(this::mapToBalanceEntry)
                        .collect(Collectors.toList());
                return TrialBalanceReportResponse.builder()
                        .startDate(startDateTime.toLocalDate())
                        .endDate(endDateTime.toLocalDate())
                        .totalDebit((BigDecimal) totals.get("total_debit"))
                        .totalCredit((BigDecimal) totals.get("total_credit"))
                        .entries(items)
                        .build();
        }
        public List<GeneralJournalReportResponse> generateGeneralJournalReport(Integer branchId, LocalDate startDate, LocalDate endDate) {

                if (startDate.isAfter(endDate)) {
                        throw new IllegalArgumentException("Start date must be before end date");
                }


                LocalDateTime startDateTime = startDate.atStartOfDay();
                LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

                List<Tuple> result = journalItemRepository.getTotalDebitAndCreditByAccountWithinTimeRange(branchId, startDateTime, endDateTime);
                List<GeneralJournalReportResponse> report = new LinkedList<>();
                for (Tuple tuple : result){
                        List<JournalItem> items = journalItemRepository.getJournalItemsByDate(branchId, (Date)tuple.get("date"));
                        GeneralJournalReportResponse record = GeneralJournalReportResponse.builder()
                                .date((Date)tuple.get("date"))
                                .totalCredit((BigDecimal) tuple.get("total_credit"))
                                .totalDebit((BigDecimal) tuple.get("total_debit"))
                                .entries(items.stream()
                                        .map(this::mapToGeneralJournalEntry)
                                        .collect(Collectors.toList())
                                )
                                .build();
                        report.add(record);
                        System.out.println(items.get(0));
                }
      
                return report;
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
                        .parentId(journalHeader.getParentId())
                        .kind(journalHeader.getKind().name())// Uncomment if using parentId
                        .journalItems(journalHeader.getJournalItems().stream()
                                .map(this::mapToJournalItemResponse)
                                .collect(Collectors.toList()))
                        .build();
        }

        private JournalItemResponse mapToJournalItemResponse(JournalItem journalItem) {
                return JournalItemResponse.builder()
                        .id(journalItem.getId())
                        .accountId(journalItem.getAccount().getId())
                        .accountName(journalItem.getAccount().getName())
                        .debit(journalItem.getDebit())
                        .credit(journalItem.getCredit())
                        .currencyId(journalItem.getCurrency().getId())
                        .currencyValue(journalItem.getCurrencyValue())
                        .date(journalItem.getDate())
                        .notes(journalItem.getNotes())
                        .build();
        }

        private LedgerReport.LedgerEntry mapToLedgerEntry(JournalItem journalItem) {
                return LedgerReport.LedgerEntry.builder()
                        .id(journalItem.getId())
                        .date(journalItem.getDate())
                        .debit(journalItem.getDebit())
                        .credit(journalItem.getCredit())
                        .notes(journalItem.getNotes())
                        .accountId(journalItem.getAccount().getId())
                        .accountName(journalItem.getAccount().getName())
                        .accountCode(journalItem.getAccount().getCode())
                        .build();
        }

        private GeneralJournalReportResponse.JournalEntry mapToGeneralJournalEntry(JournalItem journalItem) {
                return GeneralJournalReportResponse.JournalEntry.builder()
                        .id(journalItem.getId())
                        .accountId(journalItem.getAccount().getId())
                        .accountCode(journalItem.getAccount().getCode())
                        .accountName(journalItem.getAccount().getName())
                        .debit(journalItem.getDebit())
                        .credit(journalItem.getCredit())
                        .notes(journalItem.getNotes())
                        .build();
        }
        private TrialBalanceReportResponse.BalanceEntry mapToBalanceEntry(Tuple tuple) {
                return TrialBalanceReportResponse.BalanceEntry.builder()
                        .accountId((Integer) tuple.get("accountId"))
                        .accountCode((String) tuple.get("accountCode"))
                        .accountName((String) tuple.get("accountName"))
                        .debit((BigDecimal) tuple.get("total_debit"))
                        .credit((BigDecimal) tuple.get("total_credit"))
                        .build();
        }
}