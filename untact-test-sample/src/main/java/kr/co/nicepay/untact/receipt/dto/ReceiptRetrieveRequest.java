package kr.co.nicepay.untact.receipt.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Builder
@ToString
@RequiredArgsConstructor
public class ReceiptRetrieveRequest {
    private final String ediDate;
    private final String signData;
    private final String returnCharSet;
}
