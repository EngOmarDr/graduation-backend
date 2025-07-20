package com.graduationProject._thYear.Invoice.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InvoiceKind {
    NORMAL(0),
    TRANSFER(1);

    private final int code;

    public static InvoiceKind fromCode(int code) {
        for (InvoiceKind kind : values()) {
            if (kind.code == code) {
                return kind;
            }
        }
        throw new IllegalArgumentException("Invalid InvoiceKind code: " + code);
    }
}
