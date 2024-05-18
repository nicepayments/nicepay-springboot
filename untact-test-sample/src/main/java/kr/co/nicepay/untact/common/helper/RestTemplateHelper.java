package kr.co.nicepay.untact.common.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Map;

/**
 * <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html">RestTemplate Docs</a>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RestTemplateHelper {

    private final RestTemplate restTemplate;

    private static HttpHeaders getHttpHeaders(String header) {
        // headers
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + header);
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }

    private static HttpHeaders setHeaders(String header) {
        // headers
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + header);
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return headers;
    }

    public <T> ResponseEntity<T> postRestTemplate(
            String url, String header, Class<T> responseType
    ) {
        return postRestTemplate(url, header, "", responseType);
    }

    public <T> ResponseEntity<T> postRestTemplate(
            String url, String header, String body, Class<T> responseType
    ) {

        // headers
        HttpHeaders headers = getHttpHeaders(header);

        // body
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        log.info("url: {}, header: {}, contents: {}", url, header, body);
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);
    }

    public <T> ResponseEntity<T> postForEntity(
            String url, Map<String, Object> pathVariables, String header, String body, Class<T> responseType
    ) {
        HttpHeaders headers = getHttpHeaders(header);

        UriComponents builder = UriComponentsBuilder.fromUriString(url).buildAndExpand(pathVariables);

        // body
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        log.info("url: {}, header: {}, contents: {}", builder.toUriString(), header, body);
        return restTemplate.exchange(builder.toUriString(), HttpMethod.POST, requestEntity, responseType);
    }

    public <T> ResponseEntity<T> getParamsRestTemplate(
      String url, String header, MultiValueMap<String, Object> params, Class<T> responseType
    ) {
        HttpHeaders headers = setHeaders(header);

        HttpEntity<String> request = new HttpEntity<>(headers);

        String fullUrl = buildUrlWithParams(url, params);

        log.info("url: {}, header: {}", fullUrl, header);
        return restTemplate.exchange(fullUrl, HttpMethod.GET, request, responseType);
    }

    private String buildUrlWithParams(String url, MultiValueMap<String, Object> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if (queryParams != null) {
            for (Map.Entry<String, java.util.List<Object>> entry : queryParams.entrySet()) {
                for (Object value : entry.getValue()) {
                    builder.queryParam(entry.getKey(), value);
                }
            }
        }
        return builder.toUriString();
    }

    public <T> ResponseEntity<T> getForEntity(
            String url, Map<String, String> pathVariables, String header, Class<T> responseType
    ) {
        HttpHeaders headers = setHeaders(header);

        UriComponents builder = UriComponentsBuilder.fromUriString(url).buildAndExpand(pathVariables);

        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

        log.info("url: {}, header: {}", builder.toUriString(), header);
        return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, requestEntity, responseType);
    }
}
