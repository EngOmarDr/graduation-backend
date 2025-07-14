package com.graduationProject._thYear.InvoiceType.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Type {
    buy(1),
    sale(2),
    retrieve_buy(3),
    retrieve_sale(4),
    input(5),
    output(6);

    private final int code;

    public static Type fromCode(int code) {
        for (Type t : values()) {
            if (t.code == code) return t;
        }
        throw new IllegalArgumentException("Invalid Type code: " + code);
    }
}
