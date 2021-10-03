package com.ssd.ssd_assignment_02_v1.util;

import com.ssd.ssd_assignment_02_v1.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Util class for the HTTP rest calls
 */
@Component
public class RestClient {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${google.clientId}")
    private String clientId;

    @Value("${google.clientSecret}")
    private String clientSecret;

    @Value("${google.redirectUri}")
    private String redirectUri;

    @Value("${google.authUrl}")
    private String authUrl;
    @Value("${google.profileScope}")
    private String profileScope;

    @Value("${google.calendarScope}")
    private String calendarScope;

    /**
     * Set the header na to "Authorization"
     * Set the content type to Application/JSON
     *
     * @return HttpHeaders
     */
    private HttpHeaders getRequestHeader() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(Constants.Headers.AUTHORIZATION, Constants.Headers.BASIC.concat(EncodingUtility.getBase64EncodedString(clientId.concat(":").concat(clientSecret))));
        headers.add(Constants.Headers.CONTENT_TYPE, Constants.Headers.APPLICATION_JSON);
        return headers;
    }

    /**
     * Set the header with the token
     *
     * @param token access token
     * @return HttpHeaders
     */

    private HttpHeaders getRequestHeader(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(Constants.Headers.AUTHORIZATION, Constants.Headers.BEARER.concat(token));
        return headers;
    }

    /**
     * Generic method for GET requests with access token
     *
     * @param url      service URL
     * @param token    access token
     * @param classObj Response Type <T>
     * @param <T>      response
     * @return <T> ResponseEntity<T>
     */
    public <T> ResponseEntity<T> restExchange(String url, String token, Class<T> classObj) {
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(this.getRequestHeader(token)), classObj);
    }

    /**
     * Generic method for GET requests with the access token
     * This is suitable to the requests with complex query params
     *
     * @param url      service url
     * @param token    access token
     * @param classObj response Type <T>
     * @param <T>      response
     * @return <T> ResponseEntity<T>
     */
    public <T> ResponseEntity<T> restExchangeWithParams(URI url, String token, Class<T> classObj) {
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(this.getRequestHeader(token)), classObj);
    }

    /**
     * Retrieve the response with access token,token_type and expires_in for the given request
     *
     * @param url        request url
     * @param entity     request body
     * @param httpMethod http request method
     * @param classObj   Response Type <T>
     * @param <T>        response
     * @return <T> ResponseEntity<T>
     */
    public <T> ResponseEntity<T> tokenExchange(String url, Object entity, HttpMethod httpMethod, Class<T> classObj) {
        return restTemplate.exchange(url, httpMethod, new HttpEntity<>(entity, this.getRequestHeader()), classObj);
    }
}
