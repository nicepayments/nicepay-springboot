package kr.co.nicepay.untact.checkamt;


import kr.co.nicepay.untact.checkamt.dto.CheckAmountRequest;
import kr.co.nicepay.untact.checkamt.dto.CheckAmountResponse;
import kr.co.nicepay.untact.common.domain.Id;
import kr.co.nicepay.untact.common.helper.JsonHelper;
import kr.co.nicepay.untact.common.helper.RestTemplateHelper;
import kr.co.nicepay.untact.core.props.hosts.DomainUrlProperties;
import kr.co.nicepay.untact.core.props.merchant.MerchantProperties;
import kr.co.nicepay.untact.core.props.urls.CheckAmountUrlProperties;
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
public class CheckAmountOutboundAdapter implements CheckAmountOutboundPort {

    private final DomainUrlProperties domainUrlProperties;
    private final CheckAmountUrlProperties checkAmountUrlProperties;
    private final MerchantProperties merchantProperties;
    private final RestTemplateHelper restTemplate;
    private final JsonHelper mapper;

    @Override
    public Pair<Integer, CheckAmountResponse> checkAmount(
            final Id<String> tid,
            final CheckAmountRequest checkAmountRequest
    ) {
        log.info("tid: {}, checkAmountReq: {}", tid, checkAmountRequest);

        Map<String, Object> pathVariables = new HashMap<>();
        pathVariables.put("tid", tid.getValue());

        ResponseEntity<CheckAmountResponse> checkAmountResponse = restTemplate.postForEntity(
                domainUrlProperties.getApi() + checkAmountUrlProperties.getCheckAmount(),
                pathVariables,
                merchantProperties.getToken(),
                mapper.toJson(checkAmountRequest),
                CheckAmountResponse.class
        );

        if (!checkAmountResponse.getStatusCode().is2xxSuccessful()) {
            log.error("CheckAmountRestTemplateUseCase.checkAmount: {}", checkAmountResponse);
            throw new RuntimeException("승인 금액검증 실패");
        }

        return Pair.of(
                checkAmountResponse.getStatusCode().value(),
                checkAmountResponse.getBody()
        );
    }
}
