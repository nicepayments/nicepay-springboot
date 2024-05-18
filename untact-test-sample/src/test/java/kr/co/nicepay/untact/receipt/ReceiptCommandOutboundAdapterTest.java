package kr.co.nicepay.untact.receipt;


import kr.co.nicepay.untact.common.domain.Id;
import kr.co.nicepay.untact.common.types.PaymentMethod;
import kr.co.nicepay.untact.common.types.PaymentStatus;
import kr.co.nicepay.untact.common.types.ReceiptType;
import kr.co.nicepay.untact.core.CardInfTest;
import kr.co.nicepay.untact.receipt.dto.*;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest
@DisplayName("현금영수증 시나리오 테스트")
@TestInstance(PER_CLASS)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class ReceiptCommandOutboundAdapterTest extends CardInfTest {

    private static final Logger log = LoggerFactory.getLogger(ReceiptCommandOutboundAdapterTest.class);
    private static Id<String> issueReceiptTid;
    @Autowired
    private ReceiptCommandOutboundAdapter receiptCommandRestClient;
    @Autowired
    private ReceiptQueryOutboundAdapter receiptQueryRestClient;

    @Order(1)
    @DisplayName("현금영수증 발급 테스트")
    @Test
    void issueReceiptTest() {

        // given
        String orderId = UUID.randomUUID().toString().replaceAll("-", "");

        ReceiptCreateRequest receiptCreateRequest = ReceiptCreateRequest.builder()
                .orderId(orderId)
                .amount(BigDecimal.valueOf(1004))
                .goodsName("현금 영수증 발급 테스트")
                .receiptType(ReceiptType.INDIVIDUAL)
                .receiptNo("01000000000")
                .supplyAmt(904)
                .goodsVat(100)
                .taxFreeAmt(0)
                .serviceAmt(0)
                .build();

        // when
        Pair<Integer, ReceiptCreateResponse> issueReceiptResponse = receiptCommandRestClient.issue(receiptCreateRequest);
        log.info("{}", issueReceiptResponse);

        assertThat(issueReceiptResponse.getFirst()).isEqualTo(HttpStatus.OK.value());

        ReceiptCreateResponse issueReceipt = issueReceiptResponse.getSecond();

        assertThat(issueReceipt.getResultCode()).isEqualTo("0000");
        assertThat(issueReceipt.getResultMsg()).contains("현금영수증 처리 성공");

        issueReceiptTid = issueReceipt.getTid();
    }

    @Order(2)
    @DisplayName("현금 영수증 발급 상태 조회 테스트")
    @Test
    void retrieveReceiptTest() {

        ReceiptRetrieveRequest receiptRetrieveRequest = ReceiptRetrieveRequest.builder()
                .returnCharSet(StandardCharsets.UTF_8.name())
                .build();

        Pair<Integer, ReceiptRetrieveResponse> retrieveReceiptResponse = receiptQueryRestClient.retrieve(
                issueReceiptTid, receiptRetrieveRequest
        );

        log.info("{}", retrieveReceiptResponse);

        assertThat(retrieveReceiptResponse.getFirst()).isEqualTo(HttpStatus.OK.value());

        ReceiptRetrieveResponse retrieveReceipt = retrieveReceiptResponse.getSecond();

        assertThat(retrieveReceipt.getResultCode()).isEqualTo("0000");
        assertThat(retrieveReceipt.getResultMsg()).isEqualTo("정상 처리되었습니다.");
        assertThat(retrieveReceipt.getPayMethod()).isEqualTo(PaymentMethod.CASH_RECEIPT);
        assertThat(retrieveReceipt.getStatus()).isEqualTo(PaymentStatus.ISSUE_REQUESTED);
    }

    @Order(3)
    @DisplayName("현금 영수증 발급 취소 테스트")
    @Test
    void cancelReceiptTest() {
        // given
        ReceiptCancelRequest receiptCancelRequest = ReceiptCancelRequest.builder()
                .orderId(UUID.randomUUID().toString())
                .reason("현금 영수증 취소 테스트")
                .build();

        // when
        Pair<Integer, ReceiptCancelResponse> cancelReceiptResponse = receiptCommandRestClient.cancel(
                issueReceiptTid, receiptCancelRequest
        );

        log.info("{}", cancelReceiptResponse);

        // then
        assertThat(cancelReceiptResponse.getFirst()).isEqualTo(HttpStatus.OK.value());

        ReceiptCancelResponse cancelReceipt = cancelReceiptResponse.getSecond();

        assertThat(cancelReceipt.getResultCode()).isEqualTo("0000");
        assertThat(cancelReceipt.getResultMsg()).contains("취소 성공");
    }

    @Order(4)
    @DisplayName("현금 영수증 발급 취소 상태 조회 테스트")
    @Test
    void retrieveReceiptWithCancelledTest() {
        ReceiptRetrieveRequest receiptRetrieveRequest = ReceiptRetrieveRequest.builder()
                .returnCharSet(StandardCharsets.UTF_8.name())
                .build();

        Pair<Integer, ReceiptRetrieveResponse> retrieveReceiptResponse = receiptQueryRestClient.retrieve(
                issueReceiptTid, receiptRetrieveRequest
        );

        log.info("{}", retrieveReceiptResponse);

        assertThat(retrieveReceiptResponse.getFirst()).isEqualTo(HttpStatus.OK.value());

        ReceiptRetrieveResponse retrieveReceipt = retrieveReceiptResponse.getSecond();

        assertThat(retrieveReceipt.getResultCode()).isEqualTo("0000");
        assertThat(retrieveReceipt.getResultMsg()).contains("정상 처리되었습니다.");
        assertThat(retrieveReceipt.getPayMethod()).isEqualTo(PaymentMethod.CASH_RECEIPT);
    }
}