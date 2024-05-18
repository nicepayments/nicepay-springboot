package kr.co.nicepay.untact.receipt;

import kr.co.nicepay.untact.common.domain.Id;
import kr.co.nicepay.untact.common.helper.JsonHelper;
import kr.co.nicepay.untact.common.helper.RestTemplateHelper;
import kr.co.nicepay.untact.core.props.hosts.DomainUrlProperties;
import kr.co.nicepay.untact.core.props.merchant.MerchantProperties;
import kr.co.nicepay.untact.core.props.urls.ReceiptUrlProperties;
import kr.co.nicepay.untact.receipt.dto.ReceiptCancelRequest;
import kr.co.nicepay.untact.receipt.dto.ReceiptCancelResponse;
import kr.co.nicepay.untact.receipt.dto.ReceiptCreateRequest;
import kr.co.nicepay.untact.receipt.dto.ReceiptCreateResponse;
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
public class ReceiptCommandOutboundAdapter implements ReceiptCommandOutboundPort {

    private final DomainUrlProperties domainUrlProperties;
    private final ReceiptUrlProperties receiptUrlProperties;
    private final MerchantProperties merchantProperties;
    private final RestTemplateHelper restTemplate;
    private final JsonHelper mapper;

    @Override
    public Pair<Integer, ReceiptCreateResponse> issue(
            final ReceiptCreateRequest receiptCreateRequest
    ) {
        log.info("ReceiptRestTempleUseCase.issue : {}", receiptCreateRequest);

        ResponseEntity<ReceiptCreateResponse> issueReceipt = restTemplate.postRestTemplate(
                domainUrlProperties.getApi() + receiptUrlProperties.getIssue(),
                merchantProperties.getToken(),
                mapper.toJson(receiptCreateRequest),
                ReceiptCreateResponse.class
        );

        if (!issueReceipt.getStatusCode().is2xxSuccessful()) {
            log.error("ReceiptRestTempleClient.issue: {}", issueReceipt);
            throw new RuntimeException("영수증 발급 실패");
        }

        return Pair.of(
                issueReceipt.getStatusCode().value(),
                issueReceipt.getBody()
        );
    }

    @Override
    public Pair<Integer, ReceiptCancelResponse> cancel(
            final Id<String> tid, final ReceiptCancelRequest receiptCancelRequest
    ) {
        log.info("ReceiptRestTempleUseCase.cancel : {}", receiptCancelRequest);


        Map<String, Object> pathVariable = new HashMap<>();
        pathVariable.put("tid", tid.getValue());

        ResponseEntity<ReceiptCancelResponse> cancelReceipt = restTemplate.postForEntity(
                domainUrlProperties.getApi() + receiptUrlProperties.getCancel(),
                pathVariable,
                merchantProperties.getToken(),
                mapper.toJson(receiptCancelRequest),
                ReceiptCancelResponse.class
        );

        if (!cancelReceipt.getStatusCode().is2xxSuccessful()) {
            log.error("ReceiptRestTempleClient.cancel: {}", cancelReceipt);
            throw new RuntimeException("영수증 취소 실패");
        }

        return Pair.of(
                cancelReceipt.getStatusCode().value(),
                cancelReceipt.getBody()
        );
    }
}
