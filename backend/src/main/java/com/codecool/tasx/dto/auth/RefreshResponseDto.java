package com.codecool.tasx.dto.auth;

public record RefreshResponseDto(String accessToken, UserInfoDto userInfo) {
}
