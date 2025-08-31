package com.graduationProject._thYear.Invoice.annotationValidator;

import com.graduationProject._thYear.Invoice.dtos.requests.CreateInvoiceItemRequest;
import com.graduationProject._thYear.Invoice.dtos.requests.CreateInvoiceRequest;
import com.graduationProject._thYear.Invoice.dtos.requests.UpdateInvoiceItemRequest;
import com.graduationProject._thYear.Invoice.dtos.requests.UpdateInvoiceRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class UniqueInvoiceItemProductValidator implements ConstraintValidator<UniqueInvoiceItemProducts, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof CreateInvoiceRequest request) {
            return checkItems(request.getInvoiceItems(), context);
        } else if (value instanceof UpdateInvoiceRequest request) {
            return checkItems(request.getInvoiceItems(), context);
        }
        return true; // not applicable
    }

    private <T> boolean checkItems(List<T> invoiceItems, ConstraintValidatorContext context) {
        if (invoiceItems == null || invoiceItems.isEmpty()) {
            return true;
        }

        Set<String> seen = new HashSet<>();

        for (T item : invoiceItems) {
            Integer productId = null;
            Integer unitItemId = null;

            if (item instanceof CreateInvoiceItemRequest createItem) {
                productId = createItem.getProductId();
                unitItemId = createItem.getUnitItemId();
            } else if (item instanceof UpdateInvoiceItemRequest updateItem) {
                productId = updateItem.getProductId();
                unitItemId = updateItem.getUnitItemId();
            }

            if (productId != null) {
                String key = productId + "-" + (unitItemId == null ? "null" : unitItemId);
                if (!seen.add(key)) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(
                            "Duplicate productId " + productId + " with the same unitItemId " + unitItemId
                    ).addConstraintViolation();
                    return false;
                }
            }
        }

        return true;
    }
}
