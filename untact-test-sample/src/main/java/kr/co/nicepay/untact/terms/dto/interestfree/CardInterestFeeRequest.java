package kr.co.nicepay.untact.terms.dto.interestfree;

import kr.co.nicepay.untact.common.utils.OptionalMultiValue;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@Builder
@RequiredArgsConstructor
public class CardInterestFeeRequest {
  private final boolean useAuth;
  private final String ediDate;
  private final String mid;
  private final String signData;
  private final String returnCharSet;

  public MultiValueMap<String, Object> toMultiValueMap() {
    MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

    OptionalMultiValue.addIfNotNull(map, "useAuth", useAuth);
    OptionalMultiValue.addIfNotNull(map, "ediDate", ediDate);
    OptionalMultiValue.addIfNotNull(map, "mid", mid);
    OptionalMultiValue.addIfNotNull(map, "signData", signData);
    OptionalMultiValue.addIfNotNull(map, "returnCharSet", returnCharSet);

    return map;
  }
}
