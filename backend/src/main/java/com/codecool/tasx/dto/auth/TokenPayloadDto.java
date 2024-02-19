package com.codecool.tasx.dto.auth;

import com.codecool.tasx.model.auth.account.AccountType;

public record TokenPayloadDto(String email, AccountType accountType) {
}
