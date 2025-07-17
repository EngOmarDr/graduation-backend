package com.graduationProject._thYear.Advertisements.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAdvertisementRequest {

    @NotBlank
    private String name;

    private MultipartFile mediaUrl;

    @Min(1)
    private Integer duration; // in seconds
}
