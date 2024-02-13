package com.codecool.tasx.controller.dto.user.auth;

import com.codecool.tasx.config.auth.AccountType;

public record TokenPayloadDto(String email, AccountType accountType) {
}
