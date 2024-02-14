package com.codecool.tasx.controller.dto.user.auth;

import com.codecool.tasx.model.auth.account.AccountType;

public record TokenPayloadDto(String email, AccountType accountType) {
}
