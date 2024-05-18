package kr.co.nicepay.untact.payments;

import kr.co.nicepay.untact.common.domain.Id;
import kr.co.nicepay.untact.common.helper.RestTemplateHelper;
import kr.co.nicepay.untact.core.props.hosts.DomainUrlProperties;
import kr.co.nicepay.untact.core.props.merchant.MerchantProperties;
import kr.co.nicepay.untact.core.props.urls.PaymentUrlProperties;
import kr.co.nicepay.untact.payments.dto.PaymentsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentsQueryOutboundAdapter implements PaymentsOutboundQueryPort {

    private final DomainUrlProperties domainUrlProperties;
    private final PaymentUrlProperties paymentUrlProperties;
    private final MerchantProperties merchantProperties;
    private final RestTemplateHelper restTemplate;

    @Override
    public Pair<Integer, PaymentsResponse> retrieve(
            final Id<String> tid
    ) {
        log.info("PaymentCommandClient.retrieve : {}", tid);

        Map<String, String> pathVariable = new HashMap<>();
        pathVariable.put("tid", tid.getValue());

        ResponseEntity<PaymentsResponse> retrievePayments = restTemplate.getForEntity(
                domainUrlProperties.getApi() + paymentUrlProperties.getRetrieveTid(),
                pathVariable,
                merchantProperties.getToken(),
                PaymentsResponse.class
        );

        if (!retrievePayments.getStatusCode().is2xxSuccessful()) {
            log.error("PaymentCommandClient.retrieve : {}", retrievePayments);
            throw new RuntimeException("결제 조회 실패");
        }

        return Pair.of(
                retrievePayments.getStatusCode().value(),
                retrievePayments.getBody()
        );
    }

    @Override
    public Pair<Integer, PaymentsResponse> retrieveOrderId(
            final Id<String> orderId
    ) {
        log.info("PaymentCommandClient.retrieveOrderId : {}", orderId);

        Map<String, String> pathVariable = new HashMap<>();
        pathVariable.put("orderId", orderId.getValue());

        ResponseEntity<PaymentsResponse> retrievePayments = restTemplate.getForEntity(
                domainUrlProperties.getApi() + paymentUrlProperties.getRetrieveOrderId(),
                pathVariable,
                merchantProperties.getToken(),
                PaymentsResponse.class
        );

        if (!retrievePayments.getStatusCode().is2xxSuccessful()) {
            log.error("PaymentCommandClient.retrieveOrderId : {}", retrievePayments);
            throw new RuntimeException("결제 조회 실패");
        }

        return Pair.of(
                retrievePayments.getStatusCode().value(),
                retrievePayments.getBody()
        );
    }

}
