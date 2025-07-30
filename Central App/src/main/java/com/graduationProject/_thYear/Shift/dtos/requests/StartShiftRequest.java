package com.graduationProject._thYear.Shift.dtos.requests;

import java.math.BigDecimal;

import com.graduationProject._thYear.Auth.models.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class StartShiftRequest {
      private User user;
      private BigDecimal startCash;

}
