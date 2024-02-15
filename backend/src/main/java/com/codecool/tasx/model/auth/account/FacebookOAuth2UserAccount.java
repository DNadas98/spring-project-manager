package com.codecool.tasx.model.auth.account;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class FacebookOAuth2UserAccount
  extends OAuth2UserAccount {
  protected FacebookOAuth2UserAccount(String email) {
    super(email, AccountType.FACEBOOK);
  }
}
