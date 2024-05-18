package kr.co.nicepay.untact.payments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashReceipts {
    private String receiptTid;
    private String orgTid;
    private String status;
    private Integer amount;
    private Integer taxFreeAmt;
    private String receiptType;
    private String issueNo;
    private String receiptUrl;
}
