package com.graduationProject._thYear.Invoice.services;
import com.graduationProject._thYear.Account.models.Account;
import com.graduationProject._thYear.Account.repositories.AccountRepository;
import com.graduationProject._thYear.EventSyncronization.Records.InvoiceRecord.InvoiceDiscountRecord;
import com.graduationProject._thYear.EventSyncronization.Records.InvoiceRecord.InvoiceItemRecord;
import com.graduationProject._thYear.Invoice.dtos.requests.CreateInvoiceDiscountRequest;
import com.graduationProject._thYear.Invoice.dtos.requests.UpdateInvoiceDiscountRequest;
import com.graduationProject._thYear.Invoice.dtos.responses.InvoiceDiscountResponse;
import com.graduationProject._thYear.Invoice.models.InvoiceDiscount;
import com.graduationProject._thYear.Invoice.models.InvoiceHeader;
import com.graduationProject._thYear.Invoice.models.InvoiceItem;
import com.graduationProject._thYear.Invoice.repositories.InvoiceDiscountRepository;
import com.graduationProject._thYear.Invoice.repositories.InvoiceHeaderRepository;
import com.graduationProject._thYear.Product.models.Product;
import com.graduationProject._thYear.Unit.models.UnitItem;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceDiscountService {


    private final InvoiceDiscountRepository invoiceDiscountRepository;
    private final InvoiceHeaderRepository invoiceHeaderRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public InvoiceDiscountResponse create(CreateInvoiceDiscountRequest request) {
        InvoiceHeader invoiceHeader = invoiceHeaderRepository.findById(request.getInvoiceHeaderId())
                .orElseThrow(() -> new RuntimeException("Invoice header not found"));

        Account account = accountRepository.findById(request.getAccount())
                .orElseThrow(() -> new RuntimeException("account not found"));

        InvoiceDiscount discount = InvoiceDiscount.builder()
                .invoiceHeader(invoiceHeader)
                .account(account)
                .discount(request.getDiscount())
                .extra(request.getExtra())
                .notes(request.getNotes())
                .build();

        invoiceDiscountRepository.save(discount);
        return toResponse(discount);
    }

    public List<InvoiceDiscountResponse> getAll() {
        return invoiceDiscountRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public InvoiceDiscountResponse getById(Integer id) {
        return invoiceDiscountRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("InvoiceDiscount not found"));
    }

    @Transactional
    public InvoiceDiscountResponse update(Integer id, UpdateInvoiceDiscountRequest request) {
        InvoiceDiscount discount = invoiceDiscountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("InvoiceDiscount not found"));
        Account account = accountRepository.findById(request.getAccount())
                .orElseThrow(() -> new RuntimeException("account not found"));

        if (request.getAccount() != null) discount.setAccount(account);
        if (request.getDiscount() != null) discount.setDiscount(request.getDiscount());
        if (request.getExtra() != null) discount.setExtra(request.getExtra());
        if (request.getNotes() != null) discount.setNotes(request.getNotes());

        return toResponse(invoiceDiscountRepository.save(discount));
    }


    public void delete(Integer id) {
        invoiceDiscountRepository.deleteById(id);
    }

    public List<InvoiceDiscount> saveOrUpdateBulk(List<InvoiceDiscountRecord> records, InvoiceHeader invoiceHeader){
        return records.stream()
            .map(record -> saveOrUpdate(record, invoiceHeader))
            .collect(Collectors.toList());
    }
    
    public InvoiceDiscount saveOrUpdate(InvoiceDiscountRecord invoiceDiscountRecord, InvoiceHeader invoiceHeader){
        Account account = accountRepository.findByGlobalId(invoiceDiscountRecord.getAccountId())
            .orElseThrow(() -> new ResourceNotFoundException("account id not found for invoice discount sync"));
     
        return InvoiceDiscount.builder()
            .globalId(invoiceDiscountRecord.getGlobalId())
            .account(account)
            .discount(invoiceDiscountRecord.getDiscount())
            .extra(invoiceDiscountRecord.getExtra())
            .notes(invoiceDiscountRecord.getNotes())
            .invoiceHeader(invoiceHeader)
            .build();
    }
    private InvoiceDiscountResponse toResponse(InvoiceDiscount discount) {
        return InvoiceDiscountResponse.builder()
                .id(discount.getId())
                .invoiceHeaderId(discount.getInvoiceHeader().getId())
                .account(discount.getAccount().getId())
                .discount(discount.getDiscount())
                .extra(discount.getExtra())
                .notes(discount.getNotes())
                .build();
    }
}
