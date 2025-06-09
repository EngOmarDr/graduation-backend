package com.graduationProject._thYear.Journal.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.graduationProject._thYear.Branch.models.Branch;
import com.graduationProject._thYear.Currency.models.Currency;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class JournalHeaderResponse {
    private Integer id;
    private Branch branch;
    private LocalDateTime date;
    private BigDecimal debit;
    private BigDecimal credit;
    private Currency currency;
    private BigDecimal currencyValue;
    private Byte parentType;
    private Integer parentId;
    @Builder.Default
    private Boolean isPosted = true;
    private LocalDateTime postDate;
    private String notes;
    private List<JournalItemResponse> items;
}
