package com.graduationProject._thYear.Journal.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class JournalResponse {

    private Integer id;
    private Integer branchId;
    private LocalDateTime date;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private Integer currencyId;
    private BigDecimal currencyValue;
    private Boolean isPosted;
    private Integer parentType;
    private Integer parentId;
    private String kind;
    private List<JournalItemResponse> journalItems;
}
