package com.graduationProject._thYear.Purchase.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusType {
    supply(1),
    request(2),
    buy(3),
    receive(4);


    private final int code;

    public static StatusType fromCode(int code) {
        for (StatusType t : values()) {
            if (t.code == code) return t;
        }
        throw new IllegalArgumentException("Invalid Status code: " + code);
    }
}
