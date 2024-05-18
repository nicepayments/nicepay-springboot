package kr.co.nicepay.untact.common.utils;

import kr.co.nicepay.untact.common.functions.TripleConsumer;
import lombok.NoArgsConstructor;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class OptionalMultiValue {

  public static <T> void addIfNotNull(MultiValueMap<String, Object> map, String key, T value) {
    Optional.ofNullable(value).ifPresent(v -> map.add(key, v));
  }

  public static <T> void processAndAdd(MultiValueMap<String, Object> multiValueMap, Optional<T> optional, String key, TripleConsumer<MultiValueMap<String, Object>, String, T> action) {
    optional.ifPresent(value -> action.accept(multiValueMap, key, value));
  }
}
