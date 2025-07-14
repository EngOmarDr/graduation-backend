package com.graduationProject._thYear.InvoiceType.models;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public enum Type {
    buy ,
    sale,
    retrieve_buy,
    retrieve_sale,
    input,
    output;
}
