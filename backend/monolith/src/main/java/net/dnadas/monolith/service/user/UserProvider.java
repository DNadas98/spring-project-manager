package net.dnadas.monolith.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserProvider {
  private final RestTemplate restTemplate;

  @Transactional(readOnly = true)
  public Long getAuthenticatedUserId() {
    //TODO: implement
    return Long.valueOf(1);
  }
}
