package com.graduationProject._thYear.Shift.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.graduationProject._thYear.Auth.dtos.response.UserResponse;
import com.graduationProject._thYear.Shift.models.Shift;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShiftResponse {
    private Integer id;
    private UserResponse user;
    private LocalDateTime startDate;  
    private LocalDateTime endDate;  
    private BigDecimal startCash;
    private BigDecimal endCash;
    private BigDecimal expectedEndCash;
    private String notes;

    public static ShiftResponse fromShiftEntity(Shift shift) {
        return ShiftResponse.builder()
            .id(shift.getId())
            .user(UserResponse.fromUserEntity(shift.getUser()))
            .startDate(shift.getStartDate())
            .endDate(shift.getEndDate())
            .startCash(shift.getStartCash())
            .endCash(shift.getEndCash())
            .expectedEndCash(shift.getExpectedEndCash())
            .notes(shift.getNotes())
            .build();
   
    }
}
