package kr.co.nicepay.untact.terms;

import kr.co.nicepay.untact.common.helper.RestTemplateHelper;
import kr.co.nicepay.untact.core.props.hosts.DomainUrlProperties;
import kr.co.nicepay.untact.core.props.merchant.MerchantProperties;
import kr.co.nicepay.untact.core.props.urls.TermsUrlProperties;
import kr.co.nicepay.untact.terms.dto.event.CardEventRequest;
import kr.co.nicepay.untact.terms.dto.event.CardEventResponse;
import kr.co.nicepay.untact.terms.dto.interestfree.CardInterestFeeRequest;
import kr.co.nicepay.untact.terms.dto.interestfree.CardInterestFreeResponse;
import kr.co.nicepay.untact.terms.dto.term.TermsRequest;
import kr.co.nicepay.untact.terms.dto.term.TermsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TermsQueryOutboundAdapter implements TermsQueryOutboundPort {

    private final DomainUrlProperties domainUrlProperties;
    private final TermsUrlProperties termsUrlProperties;
    private final MerchantProperties merchantProperties;
    private final RestTemplateHelper restTemplateHelper;

    @Override
    public Pair<Integer, TermsResponse> terms(
            final TermsRequest termsRequest
    ) {
        log.info("{}", termsRequest);

        ResponseEntity<TermsResponse> termsResponse = restTemplateHelper.getParamsRestTemplate(
                domainUrlProperties.getApi() + termsUrlProperties.getTerms(),
                merchantProperties.getToken(),
                termsRequest.toMultiValueMap(),
                TermsResponse.class
        );

        if (!termsResponse.getStatusCode().is2xxSuccessful()) {
            log.error("TermsQueryClient.terms: {}", termsResponse);
            throw new RuntimeException("약관 조회 실패");
        }

        return Pair.of(
                termsResponse.getStatusCode().value(),
                termsResponse.getBody()
        );
    }

    @Override
    public Pair<Integer, CardEventResponse> cardEvent(
            final CardEventRequest cardEventRequest
    ) {
        log.info("{}", cardEventRequest);

        ResponseEntity<CardEventResponse> cardEventResponse = restTemplateHelper.getParamsRestTemplate(
                domainUrlProperties.getApi() + termsUrlProperties.getCardEvent(),
                merchantProperties.getToken(),
                cardEventRequest.toMultiValueMap(),
                CardEventResponse.class
        );

        if (!cardEventResponse.getStatusCode().is2xxSuccessful()) {
            log.error("TermsQueryClient.cardEvent: {}", cardEventResponse);
            throw new RuntimeException("카드 이벤트 조회 실패");
        }

        return Pair.of(
                cardEventResponse.getStatusCode().value(),
                cardEventResponse.getBody()
        );
    }

    @Override
    public Pair<Integer, CardInterestFreeResponse> cardInterestFree(
            final CardInterestFeeRequest cardInterestFeeRequest
    ) {
        log.info("{}", cardInterestFeeRequest);

        ResponseEntity<CardInterestFreeResponse> cardInterestFreeResponse = restTemplateHelper.getParamsRestTemplate(
                domainUrlProperties.getApi() + termsUrlProperties.getCardInterestFree(),
                merchantProperties.getToken(),
                cardInterestFeeRequest.toMultiValueMap(),
                CardInterestFreeResponse.class
        );

        if (!cardInterestFreeResponse.getStatusCode().is2xxSuccessful()) {
            log.error("TermsQueryClient.cardInterestFree: {}", cardInterestFreeResponse);
            throw new RuntimeException("카드 무이자 조회 실패");
        }

        return Pair.of(
                cardInterestFreeResponse.getStatusCode().value(),
                cardInterestFreeResponse.getBody()
        );
    }
}
