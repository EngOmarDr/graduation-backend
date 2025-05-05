package com.graduationProject._thYear.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.graduationProject._thYear.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

//    @JsonProperty("access_token")
//    private String accessToken;
//    @JsonProperty("refresh_token")
//    private String refreshToken;
    private String firstname;
    private String lastname;
    private String email;
    private Role role;
    private  String token;

}
