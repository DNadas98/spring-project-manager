package com.codecool.tasx.controller.dto.user.auth;

import com.codecool.tasx.config.auth.AccountType;
import com.codecool.tasx.model.user.GlobalRole;

import java.util.Set;

public record UserInfoDto(String username, String email, AccountType accountType,
                          Set<GlobalRole> roles) {
}
