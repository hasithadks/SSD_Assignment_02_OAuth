package com.ssd.ssd_assignment_02_v1.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class AccessTokenDTO {
    private String accessToken;
}
