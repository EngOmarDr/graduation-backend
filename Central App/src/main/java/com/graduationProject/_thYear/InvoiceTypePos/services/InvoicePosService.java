package com.graduationProject._thYear.InvoiceTypePos.services;
import com.graduationProject._thYear.InvoiceType.models.InvoiceType;
import com.graduationProject._thYear.InvoiceType.repositories.InvoiceTypeRepository;
import com.graduationProject._thYear.InvoiceTypePos.dtos.requests.InvoicePosCreateRequest;
import com.graduationProject._thYear.InvoiceTypePos.dtos.requests.InvoicePosUpdateRequest;
import com.graduationProject._thYear.InvoiceTypePos.dtos.responses.InvoicePosResponse;
import com.graduationProject._thYear.InvoiceTypePos.models.InvoicePos;
import com.graduationProject._thYear.InvoiceTypePos.repositories.InvoicePosRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoicePosService {

    private final InvoicePosRepository invoicePosRepository;
    private final InvoiceTypeRepository invoiceTypeRepository;

    public InvoicePosResponse create(InvoicePosCreateRequest request) {
        InvoiceType invoiceType = invoiceTypeRepository.findById(request.getInvoiceTypeId())
                .orElseThrow(() -> new EntityNotFoundException("InvoiceType not found"));

        InvoicePos invoicePos = InvoicePos.builder()
                .invoiceTypeId(invoiceType)
                .build();

        InvoicePos saved = invoicePosRepository.save(invoicePos);
        return mapToResponse(saved);
    }

    public InvoicePosResponse update(Integer id, InvoicePosUpdateRequest request) {
        InvoicePos existing = invoicePosRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("InvoicePos not found"));

        InvoiceType invoiceType = invoiceTypeRepository.findById(request.getInvoiceTypeId())
                .orElseThrow(() -> new EntityNotFoundException("InvoiceType not found"));

        existing.setInvoiceTypeId(invoiceType);

        InvoicePos updated = invoicePosRepository.save(existing);
        return mapToResponse(updated);
    }

    public void delete(Integer id) {
        if (!invoicePosRepository.existsById(id)) {
            throw new EntityNotFoundException("InvoicePos not found");
        }
        invoicePosRepository.deleteById(id);
    }

    public InvoicePosResponse getById(Integer id) {
        InvoicePos invoicePos = invoicePosRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("InvoicePos not found"));
        return mapToResponse(invoicePos);
    }

    public List<InvoicePosResponse> getAll() {
        return invoicePosRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private InvoicePosResponse mapToResponse(InvoicePos invoicePos) {
        InvoicePosResponse response = new InvoicePosResponse();
        response.setId(invoicePos.getId());
        response.setInvoiceTypeId(invoicePos.getInvoiceTypeId().getId());
        return response;
    }
}
