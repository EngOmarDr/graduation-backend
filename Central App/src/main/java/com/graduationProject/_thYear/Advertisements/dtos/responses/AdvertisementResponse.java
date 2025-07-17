package com.graduationProject._thYear.Advertisements.dtos.responses;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdvertisementResponse {
    private Integer id;
    private String name;
    private String mediaUrl;
    private Integer duration;
}
