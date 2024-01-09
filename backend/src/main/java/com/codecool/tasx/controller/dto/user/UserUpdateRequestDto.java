package com.codecool.tasx.controller.dto.user;

public record UserUpdateRequestDto(String username, String email, String password, long points) {
}
