package kr.co.nicepay.untact.payments.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@ToString
@RequiredArgsConstructor
public class PaymentsApproveRequest {

    private final BigDecimal amount;
    private final String ediDate;
    private final String signData;
    private final String returnCharSet;
}
