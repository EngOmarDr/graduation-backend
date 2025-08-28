package com.graduationProject._thYear.Journal.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JournalKind {
    NORMAL(0),
    JOURNAL_TYPE(1),
    INVOICE(2),
    TRANSFER(3),
    PURCHASE(4);

    private final int code;

    public static JournalKind fromCode(int code) {
        for (JournalKind kind : values()) {
            if (kind.getCode() == code) return kind;
        }
        throw new IllegalArgumentException("Invalid JournalKind code: " + code);
    }
}
