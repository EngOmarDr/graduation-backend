package com.graduationProject._thYear.InvoiceType.services;

import com.graduationProject._thYear.EventSyncronization.Records.InvoiceTypeRecord;
import com.graduationProject._thYear.InvoiceType.dtos.requests.CreateInvoiceTypeRequest;
import com.graduationProject._thYear.InvoiceType.dtos.requests.UpdateInvoiceTypeRequest;
import com.graduationProject._thYear.InvoiceType.dtos.responses.InvoiceTypeResponse;
import com.graduationProject._thYear.InvoiceType.models.InvoiceType;

import java.util.List;

public interface InvoiceTypeService {
    InvoiceTypeResponse create(CreateInvoiceTypeRequest dto);
    InvoiceTypeResponse getById(Integer id);
    List<InvoiceTypeResponse> getAll();
    InvoiceTypeResponse update(Integer id, UpdateInvoiceTypeRequest dto);
    void delete(Integer id);
    InvoiceType saveOrUpdate(InvoiceTypeRecord invoiceTypeRecord);
}
