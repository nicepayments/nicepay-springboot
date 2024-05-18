package kr.co.nicepay.untact.payments.dto;

import kr.co.nicepay.untact.common.domain.Id;
import lombok.*;

@Getter
@Builder
@ToString
@RequiredArgsConstructor
public class PaymentsNetCancelRequest {
    private final Id<String> orderId;
    private final String mallReserved;
    private final String ediDate;
    private final String signData;
    private final String returnCharSet;
}
