package kr.co.nicepay.untact.payments.dto;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bank {

    private String bankCode;
    private String bankName;

}
