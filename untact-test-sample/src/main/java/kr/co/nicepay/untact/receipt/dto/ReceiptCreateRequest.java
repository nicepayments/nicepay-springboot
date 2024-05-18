package kr.co.nicepay.untact.receipt.dto;


import kr.co.nicepay.untact.common.types.ReceiptType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@ToString
@RequiredArgsConstructor
public class ReceiptCreateRequest {
    // Mandatory
    private final String orderId;
    private final BigDecimal amount;
    private final String goodsName;
    private final ReceiptType receiptType;
    private final String receiptNo;
    private final int supplyAmt;
    private final int goodsVat;
    private final int taxFreeAmt;
    private final int serviceAmt;
    // Optional
    private final String buyerName;
    private final String buyerTel;
    private final String buyerEmail;
    private final String signData;
    private final String returnCharSet;
}
