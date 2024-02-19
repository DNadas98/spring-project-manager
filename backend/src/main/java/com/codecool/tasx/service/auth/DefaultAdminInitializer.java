package com.codecool.tasx.service.auth;

import com.codecool.tasx.model.auth.account.AccountType;
import com.codecool.tasx.model.auth.account.LocalUserAccount;
import com.codecool.tasx.model.auth.account.UserAccount;
import com.codecool.tasx.model.auth.account.UserAccountDao;
import com.codecool.tasx.model.user.ApplicationUser;
import com.codecool.tasx.model.user.ApplicationUserDao;
import com.codecool.tasx.model.user.GlobalRole;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DefaultAdminInitializer {
  private final UserAccountDao accountDao;
  private final ApplicationUserDao applicationUserDao;
  private final PasswordEncoder passwordEncoder;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Value("${BACKEND_DEFAULT_ADMIN_USERNAME}")
  private String username;

  @Value("${BACKEND_DEFAULT_ADMIN_EMAIL}")
  private String email;

  @Value("${BACKEND_DEFAULT_ADMIN_PASSWORD}")
  private String password;

  @PostConstruct
  @Transactional(rollbackFor = Exception.class)
  public void createDefaultSystemAdministratorAccount() {
    Optional<UserAccount> existingAccount = accountDao.findOneByEmailAndAccountType(
      email, AccountType.LOCAL);
    if (existingAccount.isPresent()) {
      logger.info("Default global administrator account already exists, skipping initialization");
      return;
    }
    ApplicationUser defaultAdminUser = new ApplicationUser(username);
    defaultAdminUser.addGlobalRole(GlobalRole.ADMIN);
    applicationUserDao.save(defaultAdminUser);
    String hashedPassword = passwordEncoder.encode(password);
    LocalUserAccount defaultAdminAccount = new LocalUserAccount(email, hashedPassword);
    defaultAdminAccount.setApplicationUser(defaultAdminUser);
    accountDao.save(defaultAdminAccount);
    logger.info("Default global administrator account initialized successfully");
  }
}
