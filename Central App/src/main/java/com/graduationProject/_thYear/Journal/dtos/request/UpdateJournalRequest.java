package com.graduationProject._thYear.Journal.dtos.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateJournalRequest {
    private Integer WarehouseId;

    private LocalDateTime date;

    private Integer currencyId;

    private BigDecimal currencyValue;

    private Byte parentType;
    private Integer parentId;

    @Builder.Default
    private Boolean isPosted = false;

    private List<CreateJournalItemRequest> journalItems;
}
