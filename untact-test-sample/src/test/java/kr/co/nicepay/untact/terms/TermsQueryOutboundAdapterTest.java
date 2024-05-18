package kr.co.nicepay.untact.terms;


import kr.co.nicepay.untact.core.CardInfTest;
import kr.co.nicepay.untact.terms.dto.event.CardEventRequest;
import kr.co.nicepay.untact.terms.dto.event.CardEventResponse;
import kr.co.nicepay.untact.terms.dto.interestfree.CardInterestFeeRequest;
import kr.co.nicepay.untact.terms.dto.interestfree.CardInterestFreeResponse;
import kr.co.nicepay.untact.terms.dto.term.TermsRequest;
import kr.co.nicepay.untact.terms.dto.term.TermsResponse;
import kr.co.nicepay.untact.common.types.TermsType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static kr.co.nicepay.untact.common.utils.SignDataEncrypt.encryptSHA256;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@DisplayName("약관조회 시나리오 테스트")
class TermsQueryOutboundAdapterTest extends CardInfTest {

    private static final Logger log = LoggerFactory.getLogger(TermsQueryOutboundAdapterTest.class);

    @Autowired
    private TermsQueryOutboundAdapter termsQueryRestClient;

    private static Stream<Arguments> terms() {
        return Stream.of(
                Arguments.of(TermsType.ELECTRONIC_FINANCIAL_TRANSACTIONS),
                Arguments.of(TermsType.TELECOMMUNICATION_CHARGING),
                Arguments.of(TermsType.COLLECT_PERSONAL_INFO),
                Arguments.of(TermsType.SHARING_PERSONAL_INFORMATION)
        );
    }

    @DisplayName("약관조회 테스트")
    @MethodSource(value = "terms")
    @ParameterizedTest(name = "{index} {displayName} - {0}")
    void retrieveTermsTest(TermsType termsType) {
        // given
        TermsRequest termsRequest = TermsRequest.builder()
                .termsType(termsType)
                .build();

        // when
        Pair<Integer, TermsResponse> termsResponse = termsQueryRestClient.terms(termsRequest);

        // then
        log.info("termsResponse = {}", termsResponse);

        assertThat(termsResponse.getFirst()).isEqualTo(200);

        TermsResponse terms = termsResponse.getSecond();

        assertThat(terms.getResultCode()).isEqualTo("0000");
        assertThat(terms.getResultMsg()).isEqualTo("정상 처리되었습니다.");
        assertThat(terms.getTermsTitle()).isEqualTo(termsType.getTitle());
    }

    @DisplayName("카드이벤트조회 테스트")
    @Test
    void retrieveCardEventTest() {
        // given
        String ediDate = LocalDateTime.now().toString();
        String signData = encryptSHA256(ediDate + merchantProperties.getSecretKey());

        CardEventRequest cardEventRequest = CardEventRequest.builder()
                .amount(BigDecimal.valueOf(50000))
                .useAuth(true)
                .signData(signData)
                .ediDate(ediDate)
                .build();

        // when
        Pair<Integer, CardEventResponse> cardEventResponse = termsQueryRestClient.cardEvent(cardEventRequest);

        // then
        log.info("cardEventResponse = {}", cardEventResponse);

        assertThat(cardEventResponse.getFirst()).isEqualTo(200);

        CardEventResponse cardEvent = cardEventResponse.getSecond();

        assertThat(cardEvent.getResultCode()).isEqualTo("0000");
        assertThat(cardEvent.getResultMsg()).isEqualTo("정상 처리되었습니다.");
        assertThat(cardEvent.getSignature()).isEqualTo(encryptSHA256(cardEvent.getEdiDate() + merchantProperties.getSecretKey()));
    }

    @DisplayName("카드 무이자 조회 테스트")
    @Test
    void retrieveCardInterestTest() {
        // given
        String ediDate = LocalDateTime.now().toString();
        String signData = encryptSHA256(ediDate + merchantProperties.getSecretKey());

        CardInterestFeeRequest cardInterestFeeRequest = CardInterestFeeRequest.builder()
                .useAuth(true)
                .signData(signData)
                .ediDate(ediDate)
                .build();

        // when
        Pair<Integer, CardInterestFreeResponse> cardInterestFeeResponse = termsQueryRestClient.cardInterestFree(cardInterestFeeRequest);

        // then
        log.info("cardInterestFeeResponse = {}", cardInterestFeeResponse);

        assertThat(cardInterestFeeResponse.getFirst()).isEqualTo(200);

        CardInterestFreeResponse cardInterestFree = cardInterestFeeResponse.getSecond();

        assertThat(cardInterestFree.getResultCode()).isEqualTo("0000");
        assertThat(cardInterestFree.getResultMsg()).isEqualTo("정상 처리되었습니다.");

    }
}