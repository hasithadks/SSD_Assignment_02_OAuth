package com.ssd.ssd_assignment_02_v1.service;

import com.ssd.ssd_assignment_02_v1.constants.Constants;
import com.ssd.ssd_assignment_02_v1.dto.AccessTokenDTO;
import com.ssd.ssd_assignment_02_v1.dto.AccessTokenRequestDTO;
import com.ssd.ssd_assignment_02_v1.dto.AccessTokenResponseDTO;
import com.ssd.ssd_assignment_02_v1.util.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;


@Service
public class GoogleAuthService {

    @Value("${google.accessTokenUri}")
    private String userAuthorizationUri;
    @Value("${google.redirectUri}")
    private String redirectUri;
    @Value("${google.clientId}")
    private String clientId;
    @Value("${google.clientSecret}")
    private String clientSecret;
    @Autowired
    private RestClient restClient;
    @Autowired
    private AccessTokenDTO accessTokenDTO;


    public String obtainAccessToken(String code) {

        AccessTokenRequestDTO request = new AccessTokenRequestDTO();
        request.setCode(code);
        request.setClient_id(clientId);
        request.setClient_secret(clientSecret);
        request.setRedirect_uri(redirectUri);
        request.setGrant_type(Constants.AUTHORIZATION_CODE);

        // retrieve access_token,token_type,expires_in
        AccessTokenResponseDTO response = restClient.tokenExchange(userAuthorizationUri, request, HttpMethod.POST, AccessTokenResponseDTO.class).getBody();
        accessTokenDTO.setAccessToken(response != null ? response.getAccess_token() : null);
        System.out.println(response.getAccess_token());

        return response.getToken_type();
    }
}
