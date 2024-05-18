package kr.co.nicepay.untact.payments.dto;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VBank {
  private String vbankCode;
  private String vbankName;
  private String vbankNumber;
  private String vbankExpDate;
  private String vbankHolder;
}
