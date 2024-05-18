package kr.co.nicepay.untact.payments.dto;


import kr.co.nicepay.untact.common.domain.Id;
import kr.co.nicepay.untact.common.types.PaymentMethod;
import kr.co.nicepay.untact.common.types.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@ToString
@EqualsAndHashCode(of = "orderId")
@NoArgsConstructor
@AllArgsConstructor
public class PaymentsResponse {
    private String resultCode;
    private String resultMsg;
    private String messageSource;
    private Id<String> sessionId;
    private Id<String> orderId;
    private Id<String> tid;
    private Id<String> bid;
    private Id<String> cancelledTid;
    private String ediDate;
    private String signature;
    private PaymentStatus status;
    private String paidAt;
    private String failedAt;
    private String cancelledAt;
    private PaymentMethod payMethod;

    private BigDecimal amount;
    private BigDecimal balanceAmt;
    private String goodsName;

    private String currency;
    private String mallReserved;
    private String mallUserId;
    private boolean useEscrow;
    private String channel;
    private String approveNo;
    private String buyerName;
    private String buyerEmail;
    private String buyerTel;
    private String receiptUrl;
    private boolean issuedCashReceipt;
    private Coupon coupon;
    private Card card;
    private Bank bank;
    private VBank vbank;
    private CashReceipts cashReceipts;
    private List<Cancel> cancels;
}
