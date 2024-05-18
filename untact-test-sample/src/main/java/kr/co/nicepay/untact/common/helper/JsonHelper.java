package kr.co.nicepay.untact.common.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonHelper {

    private final ObjectMapper mapper;

    public String toJson(Object object) {
        try {
            if (object == null) {
                throw new IllegalArgumentException("Object cannot be null");
            }
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert object to JSON string", e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid object type for JSON conversion", e);
        } catch (NullPointerException e) {
            throw new NullPointerException("Object for JSON conversion cannot be null");
        }
    }
}
