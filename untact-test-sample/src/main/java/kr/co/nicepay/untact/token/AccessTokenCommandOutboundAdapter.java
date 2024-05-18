package kr.co.nicepay.untact.token;


import kr.co.nicepay.untact.common.helper.RestTemplateHelper;
import kr.co.nicepay.untact.core.props.hosts.DomainUrlProperties;
import kr.co.nicepay.untact.core.props.merchant.MerchantProperties;
import kr.co.nicepay.untact.core.props.urls.AccessTokenUrlProperties;
import kr.co.nicepay.untact.token.dto.AccessTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessTokenCommandOutboundAdapter {

    private final DomainUrlProperties domainUrlProperties;
    private final AccessTokenUrlProperties accessTokenUrlProperties;
    private final MerchantProperties merchantProperties;
    private final RestTemplateHelper restTemplateHelper;

    /**
     * 토큰 발급 외부 통신 처리
     *
     * @return bearer 토큰 발급 응답 정보
     */
    public Pair<Integer, AccessTokenResponse> issueAccessToken() {
        log.info("AccessTokenCommandClient.issueAccessToken");

        ResponseEntity<AccessTokenResponse> issueToken = restTemplateHelper.postRestTemplate(
                domainUrlProperties.getApi() + accessTokenUrlProperties.getIssue(),
                // server 승인, bearer 인증 토큰인가?
                merchantProperties.getToken(),
                AccessTokenResponse.class
        );

        if (!issueToken.getStatusCode().is2xxSuccessful()) {
            log.warn("issueAccessToken : {}", issueToken);
        }

        return Pair.of(
                issueToken.getStatusCode().value(),
                issueToken.getBody()
        );
    }
}
