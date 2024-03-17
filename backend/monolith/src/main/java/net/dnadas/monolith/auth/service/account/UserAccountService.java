package net.dnadas.monolith.auth.service.account;

import lombok.RequiredArgsConstructor;
import net.dnadas.monolith.auth.dto.account.UserAccountResponseDto;
import net.dnadas.monolith.auth.exception.account.AccountNotFound;
import net.dnadas.monolith.auth.exception.account.OnlyOneAccountFoundException;
import net.dnadas.monolith.auth.model.account.UserAccount;
import net.dnadas.monolith.auth.model.account.UserAccountDao;
import net.dnadas.monolith.auth.model.user.ApplicationUser;
import net.dnadas.monolith.auth.service.user.UserProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserAccountService {
  private final UserAccountDao userAccountDao;
  private final UserAccountConverter userAccountConverter;
  private final UserProvider userProvider;

  @Transactional(readOnly = true)
  public Set<UserAccountResponseDto> findAllOfApplicationUser() {
    ApplicationUser applicationUser = userProvider.getAuthenticatedUser();
    Set<UserAccount> userAccounts = userAccountDao.findAllByApplicationUser(applicationUser);
    return userAccountConverter.toUserAccountResponseDtos(userAccounts);
  }

  @Transactional(rollbackFor = Exception.class)
  public void deleteOwnUserAccountById(Long id) {
    ApplicationUser applicationUser = userProvider.getAuthenticatedUser();
    if (applicationUser.getAccounts().size() == 1) {
      throw new OnlyOneAccountFoundException();
    }
    UserAccount userAccountToDelete = userAccountDao.findByIdAndApplicationUser(
        id, applicationUser)
      .orElseThrow(() -> new AccountNotFound(id));
    if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals(
      userAccountToDelete)) {
      SecurityContextHolder.clearContext();
    }
    userAccountDao.deleteOneById(id);
  }
}
