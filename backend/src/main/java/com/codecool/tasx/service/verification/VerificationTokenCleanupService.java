package com.codecool.tasx.service.verification;

import com.codecool.tasx.model.verification.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VerificationTokenCleanupService {
  private static final long TOKEN_CLEANUP_SCHEDULE_RATE_MS = 1000 * 60 * 60;
  private static final long TOKEN_EXPIRATION_HOURS = 1;
  private final VerificationTokenRepository tokenRepository;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Scheduled(fixedRate = TOKEN_CLEANUP_SCHEDULE_RATE_MS)
  @Transactional(rollbackOn = Exception.class)
  public void cleanExpiredTokens() {
    try {
      tokenRepository.deleteAllExpired(LocalDateTime.now().minusHours(TOKEN_EXPIRATION_HOURS));

      logger.info(String.format(
        "Scheduled job to clear expired verification tokens finished successfully, next execution at %s",
        LocalDateTime.now().plusHours(TOKEN_EXPIRATION_HOURS)));
    } catch (Exception e) {
      logger.error(
        String.format(
          "Scheduled job to clear expired verification tokens failed, error: %s",
          e.getMessage() != null ? e.getMessage() : "Unknown"));
    }
  }
}
