package kr.co.nicepay.untact.subscribe;

import kr.co.nicepay.untact.checkamt.CheckAmountOutboundAdapter;
import kr.co.nicepay.untact.checkamt.dto.CheckAmountRequest;
import kr.co.nicepay.untact.checkamt.dto.CheckAmountResponse;
import kr.co.nicepay.untact.common.domain.Id;
import kr.co.nicepay.untact.common.utils.SignDataEncrypt;
import kr.co.nicepay.untact.core.CardInfTest;
import kr.co.nicepay.untact.payments.PaymentsCommandOutboundAdapter;
import kr.co.nicepay.untact.payments.PaymentsQueryOutboundAdapter;
import kr.co.nicepay.untact.payments.dto.PaymentsCancelRequest;
import kr.co.nicepay.untact.payments.dto.PaymentsResponse;
import kr.co.nicepay.untact.subscribe.dto.*;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@ActiveProfiles("prod")
@DisplayName("정기결제 시나리오 테스트")
@TestInstance(PER_CLASS)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class SubscribeOutboundAdapterTest extends CardInfTest {
    private static final Logger log = LoggerFactory.getLogger(SubscribeOutboundAdapterTest.class);
    private static final BigDecimal amount = BigDecimal.valueOf(1004);
    private static Id<String> bid;
    private static Id<String> tid;
    private static Id<String> retrieveOrderId;
    @Autowired
    private SubscribeOutboundAdapter subscribeOutboundAdapter;
    @Autowired
    private CheckAmountOutboundAdapter checkAmountQueryRestClient;
    @Autowired
    private PaymentsQueryOutboundAdapter paymentsQueryOutboundAdapter;
    @Autowired
    private PaymentsCommandOutboundAdapter paymentsCommandOutboundAdapter;

    @Order(1)
    @DisplayName("정기결제 빌키발급 테스트")
    @Test
    void issueBillingTest() {
        // given
        Id<String> orderId = new Id<>(UUID.randomUUID().toString().replace("-", ""));

        SubscribeCreateRequest subscribeCreateRequest =
                new SubscribeCreateRequest
                        .Builder(orderId)
                        .encData(
                                creditCard() /* 테스트용 카드정보 */,
                                merchantProperties.getSecretKey())
//                        .encMode(EncMode.A1) /* AES128(default) or AES256 */
                        .returnCharSet(StandardCharsets.UTF_8.name())
                        .build();

        // when
        SubscribeCreateResponse issueResponse = subscribeOutboundAdapter.issue(subscribeCreateRequest);

        // then
        log.info("{}", issueResponse);
        assertThat(issueResponse.getResultCode()).isEqualTo("0000");
        assertThat(issueResponse.getBid()).isNotNull();

        bid = issueResponse.getBid();

        log.info("bid: {}", bid);
    }

    @Order(2)
    @DisplayName("정기결제 빌키승인 테스트")
    @Test
    void PaymentByBidTest() {
        // given
        Id<String> orderId = new Id<>(UUID.randomUUID().toString().replace("-", ""));

        SubscribePaymentsRequest subscribePaymentsRequest = SubscribePaymentsRequest.builder()
                .orderId(orderId)
                .amount(amount)
                .goodsName("정기결제 테스트")
                .build();

        // when
        PaymentsResponse paymentsResponse = subscribeOutboundAdapter.payments(bid, subscribePaymentsRequest);

        // then
        log.info("{}", paymentsResponse);
        assertThat(paymentsResponse.getResultCode()).isEqualTo("0000");

        tid = paymentsResponse.getTid();
        retrieveOrderId = paymentsResponse.getOrderId();

        log.info("tid: {}", tid);
        log.info("retrieveOrderId: {}", retrieveOrderId);
    }

    @Order(3)
    @DisplayName("승인금액검증 테스트")
    @Test
    void checkAmountPaymentTest() {
        // given
        String ediDate = LocalDateTime.now().toString();
        String signData = SignDataEncrypt.encryptSHA256(tid.getValue() + amount + ediDate + merchantProperties.getSecretKey());

        CheckAmountRequest checkAmountRequest = CheckAmountRequest.builder()
                .amount(amount)
                .ediDate(ediDate)
                .signData(signData)
                .build();

        // when
        Pair<Integer, CheckAmountResponse> checkAmountResponse = checkAmountQueryRestClient.checkAmount(tid, checkAmountRequest);

        // then
        log.info("{}", checkAmountResponse);
        assertThat(checkAmountResponse.getFirst()).isEqualTo(HttpStatus.OK.value());
        CheckAmountResponse checkAmt = checkAmountResponse.getSecond();

        assertThat(checkAmt.getResultCode()).isEqualTo("0000");

        log.info("checkAmt: {}", checkAmt);
    }

    @Order(4)
    @DisplayName("주문번호로 거래건 조회 테스트")
    @Test
    void retrieveTransactionByOrderId() {
        // given

        // when
        Pair<Integer, PaymentsResponse> retrieveTransactionOrderId = paymentsQueryOutboundAdapter.retrieveOrderId(retrieveOrderId);

        // then
        log.info("{}", retrieveTransactionOrderId);
        assertThat(retrieveTransactionOrderId.getFirst()).isEqualTo(HttpStatus.OK.value());
        PaymentsResponse paymentsResponse = retrieveTransactionOrderId.getSecond();

        assertThat(paymentsResponse.getResultCode()).isEqualTo("0000");

        log.info("paymentsRes: {}", paymentsResponse);
    }

    @Order(5)
    @DisplayName("정기결제를 통해 승인된 TID로 결제취소 테스트")
    @Test
    void cancelByTid() {
        // given
        Id<String> orderId = new Id<>(UUID.randomUUID().toString().replace("-", ""));

        PaymentsCancelRequest paymentsCancelRequest = PaymentsCancelRequest.builder()
                .orderId(orderId)
                .reason("빌키 승인 건 삭제 테스트")
                .build();

        // when
        Pair<Integer, PaymentsResponse> cancelledResposne = paymentsCommandOutboundAdapter.cancel(tid, paymentsCancelRequest);

        // then
        log.info("{}", cancelledResposne);
        assertThat(cancelledResposne.getFirst()).isEqualTo(HttpStatus.OK.value());
        PaymentsResponse paymentsResponse = cancelledResposne.getSecond();

        assertThat(paymentsResponse.getResultCode()).isEqualTo("0000");

        log.info("paymentsRes: {}", paymentsResponse);
    }

    @Order(6)
    @DisplayName("정기결제 빌키 삭제 테스트")
    @Test
    void expiredBillingTest() {
        // given
        Id<String> orderId = new Id<>(UUID.randomUUID().toString().replace("-", ""));

        SubscribeExpireRequest subscribeExpireRequest = SubscribeExpireRequest.builder()
                .orderId(orderId)
                .returnCharSet(StandardCharsets.UTF_8.name())
                .build();
        // when
        SubscribeExpireResponse expireResponse = subscribeOutboundAdapter.expire(bid, subscribeExpireRequest);

        // then
        log.info("{}", expireResponse);

        assertThat(expireResponse.getResultCode()).isEqualTo("0000");

        log.info("expire: {}", expireResponse);
    }
}