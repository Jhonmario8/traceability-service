package com.pragma.traceability.domain.spi;

import java.util.Map;

public interface ITokenServicePort {
    Map<String, Object> validateToken(String token);
}
