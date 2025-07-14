package com.graduationProject._thYear.Invoice.annotationValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueInvoiceItemProductValidator.class)
public @interface UniqueInvoiceItemProducts {
    String message() default "Each productId in invoiceItems must be unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}