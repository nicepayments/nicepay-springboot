package kr.co.nicepay.untact.core.config;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import kr.co.nicepay.untact.core.interceptor.LoggingRequestInterceptor;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class WebConfiguration {

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
    return builder -> {
      // SerializationFeature.FAIL_ON_EMPTY_BEANS은 기본 getter가 없는 경우 실패를 방지하기 위해 사용
      builder.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

      // DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES를 비활성화하면 JSON에 있는 알 수 없는 속성이 Java 객체에 매핑되지 않을 때 예외가 발생하지 않음
      builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

      // setSerializationInclusion(JsonInclude.Include.NON_NULL)과 같은 설정을 통해 null 값이 있는 필드를 JSON 출력에서 제외
      builder.serializationInclusion(JsonInclude.Include.NON_NULL);
    };
  }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                // 5초 동안 서버와 연결이 안되면 예외 발생
                .setConnectTimeout(Duration.ofSeconds(5))
                // 30초 동안 응답이 없으면 예외 발생
                .setReadTimeout(Duration.ofSeconds(30))
                .additionalInterceptors(new LoggingRequestInterceptor())
                .build();
    }
}
