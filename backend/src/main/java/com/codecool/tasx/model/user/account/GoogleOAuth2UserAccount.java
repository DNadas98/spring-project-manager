package com.codecool.tasx.model.user.account;

import com.codecool.tasx.config.auth.AccountType;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class GoogleOAuth2UserAccount
  extends OAuth2UserAccount {
  public GoogleOAuth2UserAccount(String email) {
    super(email, AccountType.GOOGLE);
  }
}
