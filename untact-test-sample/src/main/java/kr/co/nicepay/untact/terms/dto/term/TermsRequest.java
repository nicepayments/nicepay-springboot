package kr.co.nicepay.untact.terms.dto.term;

import kr.co.nicepay.untact.common.types.TermsType;
import kr.co.nicepay.untact.common.utils.OptionalMultiValue;
import lombok.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

@Getter
@Builder
@RequiredArgsConstructor
public class TermsRequest {

  private final TermsType termsType;
  private final String returnCharSet;

  public MultiValueMap<String, Object> toMultiValueMap() {
    MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

    OptionalMultiValue.processAndAdd(
      map, Optional.ofNullable(termsType), "termsType", (m, key, value) -> m.add(key, value.getCode()));
    OptionalMultiValue.addIfNotNull(map, "returnCharSet", returnCharSet);

    return map;
  }
}
