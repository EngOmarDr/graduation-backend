package com.graduationProject._thYear.Journal.dtos.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateJournalRequest {
    @NotNull(message = "Warehouse id is required")
    private Integer branchId;

    @NotNull(message = "Date is required")
    private LocalDateTime date;

    @NotNull(message = "Currency id is required")
    private Integer currencyId;

    @NotNull(message = "Currency value is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be positive")
    private BigDecimal currencyValue;

    private Integer kind;

    private Integer parentType;
    private Integer parentId;

    @Builder.Default
    private Boolean isPosted = false;

    @NotNull(message = "Journal items are required")
    private List<CreateJournalItemRequest> journalItems;
}
