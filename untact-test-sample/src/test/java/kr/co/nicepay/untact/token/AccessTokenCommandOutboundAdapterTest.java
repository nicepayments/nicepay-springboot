package kr.co.nicepay.untact.token;

import kr.co.nicepay.untact.token.dto.AccessTokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

@DisplayName("[AccessTokenCommandClient] - 토큰 발급")
@SpringBootTest
class AccessTokenCommandOutboundAdapterTest {

    @Autowired
    private AccessTokenCommandOutboundAdapter accessTokenCommandOutboundAdapter;

    @DisplayName("bearer 토큰발급 테스트")
    @Test
    void issueAccessTokenTest() {
        // when
        Pair<Integer, AccessTokenResponse> issueAccessTokenRes = accessTokenCommandOutboundAdapter.issueAccessToken();

        // then
        assertThat(issueAccessTokenRes.getFirst()).isEqualTo(200);
        AccessTokenResponse issueToken = issueAccessTokenRes.getSecond();

        assertThat(issueToken.getAccessToken()).isNotNull();
        assertThat(issueToken.getTokenType()).isEqualTo("Bearer");

        String expireAt = issueToken.getExpiredAt();
        String now = issueToken.getNow();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        assertThatNoException().isThrownBy(() -> ZonedDateTime.parse(expireAt, formatter));
        assertThatNoException().isThrownBy(() -> ZonedDateTime.parse(now, formatter));

    }
}