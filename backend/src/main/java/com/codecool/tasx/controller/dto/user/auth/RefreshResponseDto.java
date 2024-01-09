package com.codecool.tasx.controller.dto.user.auth;

public record RefreshResponseDto(String accessToken, UserInfoDto userInfo) {
}
