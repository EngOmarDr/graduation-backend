package com.graduationProject._thYear.Invoice.annotationValidator;

import com.graduationProject._thYear.Invoice.dtos.requests.CreateInvoiceItemRequest;
import com.graduationProject._thYear.Invoice.dtos.requests.CreateInvoiceRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.Set;

public class UniqueInvoiceItemProductValidator implements ConstraintValidator<UniqueInvoiceItemProducts, CreateInvoiceRequest> {

    @Override
    public boolean isValid(CreateInvoiceRequest request, ConstraintValidatorContext context) {
        if (request.getInvoiceItems() == null) return true;

        Set<Integer> seen = new HashSet<>();
        for (CreateInvoiceItemRequest item : request.getInvoiceItems()) {
            Integer productId = item.getProductId();
            if (productId != null && !seen.add(productId)) {
                return false; // duplicate detected
            }
        }
        return true;
    }
}

