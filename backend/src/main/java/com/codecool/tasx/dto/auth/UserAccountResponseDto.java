package com.codecool.tasx.dto.auth;

import com.codecool.tasx.model.auth.account.AccountType;

public record UserAccountResponseDto(Long id, String email, AccountType accountType) {
}
