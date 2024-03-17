package net.dnadas.monolith.auth.service.account;

import net.dnadas.monolith.auth.dto.account.UserAccountResponseDto;
import net.dnadas.monolith.auth.model.account.UserAccount;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserAccountConverter {
  public UserAccountResponseDto toUserAccountResponseDto(UserAccount userAccount) {
    return new UserAccountResponseDto(userAccount.getId(), userAccount.getEmail(),
      userAccount.getAccountType());
  }

  public Set<UserAccountResponseDto> toUserAccountResponseDtos(Set<UserAccount> userAccounts) {
    return userAccounts.stream().map(account -> toUserAccountResponseDto(account)).collect(
      Collectors.toSet());
  }
}
