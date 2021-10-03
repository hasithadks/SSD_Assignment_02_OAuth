package com.ssd.ssd_assignment_02_v1.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class AccessTokenRequestDTO {
    private String code;
    private String client_id;
    private String client_secret;
    private String redirect_uri;
    private String grant_type;
}
