package kr.co.nicepay.untact.terms.dto.event;

import kr.co.nicepay.untact.common.utils.OptionalMultiValue;
import lombok.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
@RequiredArgsConstructor
public class CardEventRequest {
    private final BigDecimal amount;
    private final boolean useAuth;
    private final String ediDate;
    private final String mid;
    private final String signData;
    private final String returnCharSet;

  public MultiValueMap<String, Object> toMultiValueMap() {
    MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

    OptionalMultiValue.addIfNotNull(map, "amount", amount);
    OptionalMultiValue.addIfNotNull(map, "useAuth", useAuth);
    OptionalMultiValue.addIfNotNull(map, "ediDate", ediDate);
    OptionalMultiValue.addIfNotNull(map, "mid", mid);
    OptionalMultiValue.addIfNotNull(map, "signData", signData);
    OptionalMultiValue.addIfNotNull(map, "returnCharSet", returnCharSet);

    return map;
  }
}
