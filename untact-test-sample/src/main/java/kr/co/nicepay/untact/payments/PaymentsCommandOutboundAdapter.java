package kr.co.nicepay.untact.payments;

import kr.co.nicepay.untact.common.domain.Id;
import kr.co.nicepay.untact.common.helper.JsonHelper;
import kr.co.nicepay.untact.common.helper.RestTemplateHelper;
import kr.co.nicepay.untact.core.props.hosts.DomainUrlProperties;
import kr.co.nicepay.untact.core.props.merchant.MerchantProperties;
import kr.co.nicepay.untact.core.props.urls.PaymentUrlProperties;
import kr.co.nicepay.untact.payments.dto.PaymentsApproveRequest;
import kr.co.nicepay.untact.payments.dto.PaymentsCancelRequest;
import kr.co.nicepay.untact.payments.dto.PaymentsNetCancelRequest;
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
public class PaymentsCommandOutboundAdapter implements PaymentsOutboundCommandPort {

    private final DomainUrlProperties domainUrlProperties;
    private final PaymentUrlProperties paymentUrlProperties;
    private final MerchantProperties merchantProperties;
    private final RestTemplateHelper restTemplate;
    private final JsonHelper mapper;

    @Override
    public Pair<Integer, PaymentsResponse> payments(
            final Id<String> tid,
            final PaymentsApproveRequest paymentsApproveRequest
    ) {
        log.info("PaymentCommandClient.payments : {} :: {}", tid, paymentsApproveRequest);


        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("tid", tid.getValue());

        ResponseEntity<PaymentsResponse> paymentsResponse = restTemplate.postForEntity(
                domainUrlProperties.getApi() + paymentUrlProperties.getApprove(),
                uriVariables,
                merchantProperties.getToken(),
                mapper.toJson(paymentsApproveRequest),
                PaymentsResponse.class
        );

        if (!paymentsResponse.getStatusCode().is2xxSuccessful()) {
            log.error("PaymentCommandClient.payments : {}", paymentsResponse);
            throw new RuntimeException("결제 승인 실패");
        }

        return Pair.of(
                paymentsResponse.getStatusCode().value(),
                paymentsResponse.getBody()
        );
    }

    @Override
    public Pair<Integer, PaymentsResponse> cancel(
            final Id<String> tid,
            final PaymentsCancelRequest cancelReq
    ) {
        log.info("PaymentCommandClient.cancel : {} :: {}", tid, cancelReq);


        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("tid", tid.getValue());

        ResponseEntity<PaymentsResponse> paymentsResponse = restTemplate.postForEntity(
                domainUrlProperties.getApi() + paymentUrlProperties.getCancel(),
                uriVariables,
                merchantProperties.getToken(),
                mapper.toJson(cancelReq),
                PaymentsResponse.class
        );

        if (!paymentsResponse.getStatusCode().is2xxSuccessful()) {
            log.error("PaymentCommandClient.cancel: {}", paymentsResponse);
            throw new RuntimeException("결제 취소 실패");
        }

        return Pair.of(
                paymentsResponse.getStatusCode().value(),
                paymentsResponse.getBody()
        );
    }

    @Override
    public Pair<Integer, PaymentsResponse> netCancel(PaymentsNetCancelRequest netCancelReq) {
        log.info("PaymentCommandClient.netCancel : {}", netCancelReq);

        ResponseEntity<PaymentsResponse> netCancelResponse = restTemplate.postRestTemplate(
                domainUrlProperties.getApi() + paymentUrlProperties.getNetCancel(),
                merchantProperties.getToken(),
                mapper.toJson(netCancelReq),
                PaymentsResponse.class
        );

        if (!netCancelResponse.getStatusCode().is2xxSuccessful()) {
            log.error("PaymentCommandClient.netCancel: {}", netCancelResponse);
            throw new RuntimeException("결제 망상 취소 실패");
        }

        return Pair.of(
                netCancelResponse.getStatusCode().value(),
                netCancelResponse.getBody()
        );
    }
}
