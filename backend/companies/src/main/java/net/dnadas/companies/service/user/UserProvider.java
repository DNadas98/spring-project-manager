package net.dnadas.companies.service.user;

import lombok.RequiredArgsConstructor;
import net.dnadas.auth.dto.authentication.UserInfoDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProvider {
  public Long getAuthenticatedUserId() {
    return getUserInfo().userId();
  }

  public UserInfoDto getUserInfo() {
    return (UserInfoDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}
