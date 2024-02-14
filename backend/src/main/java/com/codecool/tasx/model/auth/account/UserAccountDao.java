package com.codecool.tasx.model.auth.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountDao extends JpaRepository<UserAccount, Long> {

  Optional<UserAccount> findOneByEmail(String email);
  Optional<UserAccount> findOneByEmailAndAccountType(String email, AccountType accountType);
}
