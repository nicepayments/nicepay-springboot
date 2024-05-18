package kr.co.nicepay.untact.payments.dto;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {
  private String cardCode;
  private String cardName;
  private String cardNum;
  private int cardQuota;
  private Boolean isInterestFree;
  private String cardType;
  private Boolean canPartCancel;
  private String acquCardCode;
  private String acquCardName;
}
