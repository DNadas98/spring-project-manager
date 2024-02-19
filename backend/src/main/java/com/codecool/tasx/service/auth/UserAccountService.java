package com.codecool.tasx.service.auth;

import com.codecool.tasx.dto.auth.UserAccountResponseDto;
import com.codecool.tasx.exception.auth.AccountNotFound;
import com.codecool.tasx.model.auth.account.UserAccount;
import com.codecool.tasx.model.auth.account.UserAccountDao;
import com.codecool.tasx.model.user.ApplicationUser;
import com.codecool.tasx.service.converter.UserAccountConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserAccountService {
  private final UserAccountDao userAccountDao;
  private final UserAccountConverter userAccountConverter;
  private final UserProvider userProvider;

  public Set<UserAccountResponseDto> findAllOfApplicationUser() {
    ApplicationUser applicationUser = userProvider.getAuthenticatedUser();
    Set<UserAccount> userAccounts = userAccountDao.findAllByApplicationUser(applicationUser);
    return userAccountConverter.toUserAccountResponseDtos(userAccounts);
  }

  @Transactional(rollbackFor = Exception.class)
  public void deleteOwnUserAccountById(Long id) {
    ApplicationUser applicationUser = userProvider.getAuthenticatedUser();
    UserAccount userAccount = userAccountDao.findByIdAndApplicationUser(id, applicationUser)
      .orElseThrow(
        () -> new AccountNotFound(id)
      );
    userAccountDao.delete(userAccount);
  }
}
