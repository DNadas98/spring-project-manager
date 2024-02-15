package com.codecool.tasx.controller.dto.verification;

import java.util.UUID;

public record VerificationTokenDto(Long id, UUID verificationCode) {
}
