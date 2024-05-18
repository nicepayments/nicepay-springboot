package kr.co.nicepay.untact.token.dto;

import lombok.*;

import java.beans.ConstructorProperties;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenResponse {
    private String resultCode;
    private String resultMsg;
    private String accessToken;
    private String tokenType;
    private String expiredAt;
    private String now;
}
