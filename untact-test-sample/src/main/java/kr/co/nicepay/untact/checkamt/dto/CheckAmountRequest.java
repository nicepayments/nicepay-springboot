package kr.co.nicepay.untact.checkamt.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@ToString
@RequiredArgsConstructor
public class CheckAmountRequest {
    private final BigDecimal amount;
    private final String ediDate;
    private final String signData;
    private final String returnCharSet;
}
