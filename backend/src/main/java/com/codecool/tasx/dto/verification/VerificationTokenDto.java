package com.codecool.tasx.dto.verification;

import java.util.UUID;

public record VerificationTokenDto(Long id, UUID verificationCode) {
}
