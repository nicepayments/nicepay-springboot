package kr.co.nicepay.untact.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cancel {
    private String tid;
    private int amount;
    private String cancelledAt;
    private String reason;
    private String receiptUrl;
    private int couponAmt;
}
