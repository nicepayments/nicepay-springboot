package kr.co.nicepay.untact.subscribe;


import kr.co.nicepay.untact.common.domain.Id;
import kr.co.nicepay.untact.common.helper.JsonHelper;
import kr.co.nicepay.untact.common.helper.RestTemplateHelper;
import kr.co.nicepay.untact.core.props.hosts.DomainUrlProperties;
import kr.co.nicepay.untact.core.props.merchant.MerchantProperties;
import kr.co.nicepay.untact.core.props.urls.SubscribeUrlProperties;
import kr.co.nicepay.untact.payments.dto.PaymentsResponse;
import kr.co.nicepay.untact.subscribe.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscribeOutboundAdapter implements SubscribeOutboundPort {

    private final DomainUrlProperties domainUrlProperties;
    // 정기결제 설정 정보
    private final SubscribeUrlProperties subscribeUrlProperties;
    private final MerchantProperties merchantProperties;
    // 공통
    private final RestTemplateHelper restTemplate;
    private final JsonHelper mapper;

    @Override
    public SubscribeCreateResponse issue(final SubscribeCreateRequest subscribeCreateRequest) {
        log.info("SubscribeRestTempleUseCase.issue : {}", subscribeCreateRequest);
        return handleRequest(() -> subscribeHttpRequest(
                subscribeUrlProperties.getIssue(),
                new HashMap<>(),
                subscribeCreateRequest,
                SubscribeCreateResponse.class));
    }

    @Override
    public PaymentsResponse payments(final Id<String> bid, final SubscribePaymentsRequest subscribePaymentsRequest) {
        log.info("SubscribeRestTempleUseCase.processPayment");
        Map<String, Object> uriVariables = createUriVariables(bid);
        return handleRequest(() -> subscribeHttpRequest(
                subscribeUrlProperties.getPayments(),
                uriVariables,
                subscribePaymentsRequest,
                PaymentsResponse.class));
    }

    @Override
    public SubscribeExpireResponse expire(final Id<String> bid, final SubscribeExpireRequest subscribeExpireRequest) {
        log.info("SubscribeRestTempleUseCase.expire");
        Map<String, Object> uriVariables = createUriVariables(bid);
        return handleRequest(() -> subscribeHttpRequest(
                subscribeUrlProperties.getExpire(),
                uriVariables,
                subscribeExpireRequest,
                SubscribeExpireResponse.class));
    }

    private Map<String, Object> createUriVariables(Id<String> bid) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("bid", bid.getValue());
        return uriVariables;
    }

    private <T, R> ResponseEntity<R> subscribeHttpRequest(String uri, Map<String, Object> uriVariables, T request, Class<R> responseType) {
        return restTemplate.postForEntity(
                domainUrlProperties.getApi().concat(uri),
                uriVariables,
                merchantProperties.getToken(),
                mapper.toJson(request),
                responseType
        );
    }

    // 공통 예외 처리 메소드
    private <T> T handleRequest(Supplier<ResponseEntity<T>> requestSupplier) {
        try {
            ResponseEntity<T> response = requestSupplier.get();
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("Subscribe Rest OutBound Client Exception: {}", e.getMessage());
            throw new HttpClientErrorException(e.getStatusCode(), "Client Exception");
        } catch (HttpServerErrorException e) {
            log.error("Subscribe Rest OutBound Server Exception: {}", e.getMessage());
            throw new HttpServerErrorException(e.getStatusCode(), "Server Exception");
        } catch (ResourceAccessException e) {
            log.error("Subscribe Rest OutBound Resource Access Exception: {}", e.getMessage());
            throw new ResourceAccessException("Subscribe Rest OutBound Resource Access Exception");
        } catch (Exception e) {
            log.error("Subscribe Rest OutBound Exception: {}", e.getMessage());
            throw new RuntimeException("Subscribe Rest OutBound Exception");
        }
    }
}
