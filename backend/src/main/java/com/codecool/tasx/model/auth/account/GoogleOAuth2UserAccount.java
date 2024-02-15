package com.codecool.tasx.model.auth.account;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class GoogleOAuth2UserAccount
  extends OAuth2UserAccount {
  protected GoogleOAuth2UserAccount(String email) {
    super(email, AccountType.GOOGLE);
  }
}
