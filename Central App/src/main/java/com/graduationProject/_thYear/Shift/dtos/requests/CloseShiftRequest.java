package com.graduationProject._thYear.Shift.dtos.requests;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CloseShiftRequest {
    @NotNull(message = "End Cash Is Required.")
     private BigDecimal endCash;
     private String notes;

}
