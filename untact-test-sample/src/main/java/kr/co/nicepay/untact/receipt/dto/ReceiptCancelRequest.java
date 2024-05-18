package kr.co.nicepay.untact.receipt.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@RequiredArgsConstructor
public class ReceiptCancelRequest {
    // Mandatory
    private final String orderId;
    private final String reason;
    // Optional
    private final BigDecimal cancelAmt; // 누락: 전체취소, 부분취소시 supplyAmt, goodsVat, taxFreeAmt, serviceAmt 필수
    private final String ediDate;
    private final String signData;
    private final String returnCharSet;
    private final BigDecimal supplyAmt;
    private final BigDecimal goodsVat;
    private final BigDecimal taxFreeAmt;
    private final BigDecimal serviceAmt;
}
