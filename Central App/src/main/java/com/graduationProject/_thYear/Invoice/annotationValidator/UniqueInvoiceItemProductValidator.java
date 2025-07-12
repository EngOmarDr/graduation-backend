package com.graduationProject._thYear.Invoice.annotationValidator;

import com.graduationProject._thYear.Invoice.dtos.requests.CreateInvoiceItemRequest;
import com.graduationProject._thYear.Invoice.dtos.requests.CreateInvoiceRequest;
import com.graduationProject._thYear.Invoice.dtos.requests.UpdateInvoiceItemRequest;
import com.graduationProject._thYear.Invoice.dtos.requests.UpdateInvoiceRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueInvoiceItemProductValidator implements ConstraintValidator<UniqueInvoiceItemProducts, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof CreateInvoiceRequest request) {
            return isUnique(request.getInvoiceItems());
        } else if (value instanceof UpdateInvoiceRequest request) {
            return isUnique(request.getInvoiceItems());
        }

        return true; // Let other validators handle unsupported types
    }

    private boolean isUnique(List<? extends Object> items) {
        if (items == null) return true;

        Set<Integer> seen = new HashSet<>();
        for (Object item : items) {
            Integer productId = null;
            if (item instanceof CreateInvoiceItemRequest) {
                productId = ((CreateInvoiceItemRequest) item).getProductId();
            } else if (item instanceof UpdateInvoiceItemRequest) {
                productId = ((UpdateInvoiceItemRequest) item).getProductId();
            }

            if (productId != null && !seen.add(productId)) {
                return false; // Duplicate found
            }
        }
        return true;
    }
}
