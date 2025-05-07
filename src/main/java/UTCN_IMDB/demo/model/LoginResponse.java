package UTCN_IMDB.demo.model;

import UTCN_IMDB.demo.enums.UserRole;

public record LoginResponse(
        Boolean success,
        String errorMessage,
        String token
) {
}