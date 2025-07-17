package com.graduationProject._thYear.Journal.dtos.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.graduationProject._thYear.Journal.models.JournalKind;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
    @PositiveOrZero(message = "currencyValue must be zero or positive")
    private BigDecimal currencyValue;
    private Integer kind;
    private Integer parentType;
    private Integer parentId;

    @Builder.Default
    private Boolean isPosted = false;

    private List<CreateJournalItemRequest> journalItems;
}
