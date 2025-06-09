package com.graduationProject._thYear.Journal.services;

import com.graduationProject._thYear.Account.models.Account;
import com.graduationProject._thYear.Account.repositories.AccountRepository;
import com.graduationProject._thYear.Branch.repositories.BranchRepository;
import com.graduationProject._thYear.Currency.models.Currency;
import com.graduationProject._thYear.Currency.repositories.CurrencyRepository;
import com.graduationProject._thYear.Journal.dtos.request.CreateJournalItemRequest;
import com.graduationProject._thYear.Journal.dtos.request.CreateJournalRequest;
import com.graduationProject._thYear.Journal.dtos.request.UpdateJournalItemRequest;
import com.graduationProject._thYear.Journal.dtos.request.UpdateJournalRequest;
import com.graduationProject._thYear.Journal.dtos.response.JournalHeaderResponse;
import com.graduationProject._thYear.Journal.dtos.response.JournalItemResponse;
import com.graduationProject._thYear.Journal.dtos.response.JournalResponse;
import com.graduationProject._thYear.Journal.dtos.response.LedgerReport;
import com.graduationProject._thYear.Journal.dtos.response.LedgerReport.LedgerEntry;
import com.graduationProject._thYear.Journal.models.JournalHeader;
import com.graduationProject._thYear.Journal.models.JournalItem;
import com.graduationProject._thYear.Journal.repositories.JournalHeaderRepository;
import com.graduationProject._thYear.Journal.repositories.JournalItemRepository;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;

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
        public JournalResponse createJournal(CreateJournalRequest request) {
                var branch = branchRepository.findById(request.getJournalHeader().getBranchId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Branch Id'" + request.getJournalHeader().getBranchId()
                                                                + "' not exists"));

                var currency = currencyRepository.findById(request.getJournalHeader().getCurrencyId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Currency Id'" + request.getJournalHeader().getCurrencyId()
                                                                + "' not exists"));

                var journalHeader = new JournalHeader();
                journalHeader.setBranch(branch);
                journalHeader.setDate(request.getJournalHeader().getDate());
                // journalHeader.setTotalDebit(request.getJournalHeader().getTotalCredit());
                // journalHeader.setTotalCredit(request.getJournalHeader().getTotalCredit());
                journalHeader.setCurrency(currency);
                journalHeader.setCurrencyValue(request.getJournalHeader().getCurrencyValue());
                journalHeader.setParentType(request.getJournalHeader().getParentType());
                journalHeader.setParentId(request.getJournalHeader().getParentId());
                journalHeader.setIsPosted(request.getJournalHeader().getIsPosted());
                journalHeader.setPostDate(request.getJournalHeader().getPostDate());
                journalHeader.setNotes(request.getJournalHeader().getNotes());

                var journalItems = new ArrayList<JournalItem>();
                for (CreateJournalItemRequest journalItemRequest : request.getJournalItems()) {
                        Currency currencyItem = null;
                        Account accountItem = null;
                        if (journalItemRequest.getCurrencyId() != null) {
                                currencyItem = currencyRepository.findById(journalItemRequest.getCurrencyId())
                                                .orElseThrow(() -> new IllegalArgumentException(
                                                                "Currency Id'" + request
                                                                                .getJournalHeader().getCurrencyId()
                                                                                + "' not exists"));
                        }
                        if (journalItemRequest.getAccountId() != null) {
                                accountItem = accountRepository.findById(journalItemRequest.getAccountId())
                                                .orElseThrow(() -> new IllegalArgumentException(
                                                                "Currency Id'" + request
                                                                                .getJournalHeader().getCurrencyId()
                                                                                + "' not exists"));
                        }
                        var journalItem = new JournalItem();

                        journalItem.setJornalHeader(journalHeader);
                        journalItem.setAccount(accountItem);
                        journalItem.setDebit(journalItemRequest.getDebit());
                        journalItem.setCredit(journalItemRequest.getCredit());
                        journalItem.setCurrency(currencyItem);
                        journalItem.setCurrencyValue(journalItemRequest.getCurrencyValue());
                        journalItem.setDate(journalItemRequest.getDate());
                        journalItem.setNotes(journalItemRequest.getNotes());
                        journalItems.add(journalItem);

                }
                // TODO excute transaction
                var savedHeader = journalHeaderRepository.save(journalHeader);
                var savedItems = journalItemRepository.saveAll(journalItems);

                return convertToResponse(savedItems, savedHeader);
        }

        @Override
        public JournalResponse updateJournal(Integer id, UpdateJournalRequest request) {
                var branch = branchRepository.findById(request.getJournalHeader().getBranchId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Branch Id'" + request.getJournalHeader().getBranchId()
                                                                + "' not exists"));

                var currency = currencyRepository.findById(request.getJournalHeader().getCurrencyId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Currency Id'" + request.getJournalHeader().getCurrencyId()
                                                                + "' not exists"));

                var journalHeader = journalHeaderRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Journal header Id'" + id + "' not exists"));
                journalHeader.setBranch(branch);
                journalHeader.setDate(request.getJournalHeader().getDate());
                journalHeader.setDebit(request.getJournalHeader().getDebit());
                journalHeader.setCredit(request.getJournalHeader().getCredit());
                journalHeader.setCurrency(currency);
                journalHeader.setCurrencyValue(request.getJournalHeader().getCurrencyValue());
                journalHeader.setParentType(request.getJournalHeader().getParentType());
                journalHeader.setParentId(request.getJournalHeader().getParentId());
                journalHeader.setIsPosted(request.getJournalHeader().getIsPosted());
                journalHeader.setPostDate(request.getJournalHeader().getPostDate());
                journalHeader.setNotes(request.getJournalHeader().getNotes());

                journalHeader.getJournalItems().clear();
                for (UpdateJournalItemRequest journalItemRequest : request.getJournalItems()) {
                        Currency currencyItem = null;
                        Account accountItem = null;
                        if (journalItemRequest.getCurrencyId() != null) {
                                currencyItem = currencyRepository.findById(journalItemRequest.getCurrencyId())
                                                .orElseThrow(() -> new IllegalArgumentException(
                                                                "Currency Id'" + request.getJournalHeader()
                                                                                .getCurrencyId() + "' not exists"));
                        }
                        if (journalItemRequest.getAccountId() != null) {
                                accountItem = accountRepository.findById(journalItemRequest.getAccountId())
                                                .orElseThrow(() -> new IllegalArgumentException(
                                                                "Currency Id'" + request.getJournalHeader()
                                                                                .getCurrencyId() + "' not exists"));
                        }
                        var journalItem = new JournalItem();

                        journalItem.setJornalHeader(journalHeader);
                        journalItem.setAccount(accountItem);
                        journalItem.setDebit(journalItemRequest.getDebit());
                        journalItem.setCredit(journalItemRequest.getCredit());
                        journalItem.setCurrency(currencyItem);
                        journalItem.setCurrencyValue(journalItemRequest.getCurrencyValue());
                        journalItem.setDate(journalItemRequest.getDate());
                        journalItem.setNotes(journalItemRequest.getNotes());
                        journalHeader.addJournalItem(journalItem);

                }

                var savedHeader = journalHeaderRepository.save(journalHeader);
                return convertToResponse(savedHeader.getJournalItems(), savedHeader);
        }

        @Override
        public List<JournalHeaderResponse> getAllJournals() {
                return journalHeaderRepository.findAll().stream()
                                .map(this::convertToJournalHeaderResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public List<JournalHeaderResponse> getJournalsByDateRange(LocalDate startDate, LocalDate endDate) {
                var listData = journalHeaderRepository.findByDateBetween(
                                startDate == null ? LocalDateTime.now() : startDate.atTime(0, 0, 0),
                                endDate == null ? LocalDateTime.now() : endDate.atTime(23, 59, 59));
                return listData != null ? listData.stream()
                                .map(this::convertToJournalHeaderResponse).toList() : null;
        }

        @Override
        public JournalResponse getJournalById(Integer id) {
                var journalHeader = journalHeaderRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Journal header Id'" + id + "' not exists"));
                return convertToResponse(journalHeader);
        }

        @Override
        public void deleteJournal(Integer id) {
                var journalHeader = journalHeaderRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Journal header Id'" + id + "' not exists"));
                journalHeaderRepository.delete(journalHeader);
        }

        private JournalResponse convertToResponse(List<JournalItem> journalItems, JournalHeader journalHeader) {
                return JournalResponse.builder()
                                .createJournalHeaderResponse(this.convertToJournalHeaderResponse(journalHeader))
                                .createJournalItemResponse(journalItems.stream().map(this::convertToJournalItemResponse)
                                                .toList())
                                .build();
        }

        private JournalResponse convertToResponse(JournalHeader journalHeader) {
                return JournalResponse.builder()
                                .createJournalHeaderResponse(this.convertToJournalHeaderResponse(journalHeader))
                                .createJournalItemResponse(journalHeader.getJournalItems().stream()
                                                .map(this::convertToJournalItemResponse)
                                                .toList())
                                .build();
        }

        private JournalHeaderResponse convertToJournalHeaderResponse(JournalHeader journalHeader) {
                return JournalHeaderResponse.builder()
                                .id(journalHeader.getId())
                                .branch(journalHeader.getBranch())
                                .date(journalHeader.getDate())
                                .debit(journalHeader.getDebit())
                                .credit(journalHeader.getCredit())
                                .currency(journalHeader.getCurrency())
                                .currencyValue(journalHeader.getCurrencyValue())
                                .parentType(journalHeader.getParentType())
                                .parentId(journalHeader.getParentId())
                                .isPosted(journalHeader.getIsPosted())
                                .postDate(journalHeader.getPostDate())
                                .notes(journalHeader.getNotes())
                                .items(journalHeader.getJournalItems().stream().map(this::convertToJournalItemResponse)
                                                .toList())
                                .build();
        }

        private JournalItemResponse convertToJournalItemResponse(JournalItem journalItem) {
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
                                .notes(journalItem.getNotes())
                                .build();
        }

        @Override
        public LedgerReport generateLedgerReport(Integer accountId, LocalDate startDate, LocalDate endDate) {

                Account account = accountRepository.findById(accountId)
                                .orElseThrow(() -> new RuntimeException("Account not found"));

                // حساب الرصيد الافتتاحي
                BigDecimal openingDebit =
                                // BigDecimal
                                // .valueOf(
                                journalItemRepository.calculateDebitBetweenDates(accountId, startDate.atTime(0, 0),
                                                endDate.atTime(23, 59));
                BigDecimal openingCredit =
                                // BigDecimal
                                // .valueOf(
                                journalItemRepository.calculateCreditBetweenDates(accountId, startDate.atTime(0, 0),
                                                endDate.atTime(23, 59));
                BigDecimal openingBalance = openingDebit.subtract(openingCredit);

                // جلب الحركات خلال الفترة
                List<JournalItem> entries = journalItemRepository
                                .findEntriesByAccountAndDateRange(accountId, startDate.atTime(0, 0),
                                                endDate.atTime(23, 59));

                // حساب المجاميع
                BigDecimal totalDebit = entries.stream()
                                .map(JournalItem::getDebit)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalCredit = entries.stream()
                                .map(JournalItem::getCredit)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                // حساب الرصيد الختامي
                BigDecimal closingBalance = openingBalance.add(totalDebit).subtract(totalCredit);

                // تحويل الحركات إلى DTOs
                List<LedgerReport.LedgerEntry> entryDtos = entries.stream()
                                .map(this::convertToLedgerEntry)
                                .collect(Collectors.toList());

                return LedgerReport.builder()
                                .accountId(accountId)
                                .accountCode(account.getCode())
                                .accountName(account.getName())
                                .startDate(startDate.atTime(0, 0))
                                .endDate(endDate.atTime(23, 59))
                                .openingBalance(openingBalance)
                                .totalDebit(totalDebit)
                                .totalCredit(totalCredit)
                                .closingBalance(closingBalance)
                                .entries(entryDtos)
                                .build();
        }

        private LedgerEntry convertToLedgerEntry(JournalItem item) {
                return LedgerEntry.builder()
                                .id(item.getId())
                                .date(item.getDate())
                                .debit(item.getDebit())
                                .credit(item.getCredit())
                                .notes(item.getNotes())
                                .build();
        }

}
