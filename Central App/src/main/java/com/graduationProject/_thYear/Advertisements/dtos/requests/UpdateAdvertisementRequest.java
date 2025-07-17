package com.graduationProject._thYear.Advertisements.dtos.requests;

import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAdvertisementRequest {

    private String name;

    private MultipartFile mediaUrl;

    @Min(1)
    private Integer duration;
}
