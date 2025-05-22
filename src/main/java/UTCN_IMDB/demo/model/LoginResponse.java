package UTCN_IMDB.demo.model;

import UTCN_IMDB.demo.enums.UserRole;

import java.util.UUID;

public record LoginResponse(
        Boolean success,
        String errorMessage,
        String token,
        UUID userId
) {
}