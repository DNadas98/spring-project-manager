package com.codecool.tasx.model.auth.account;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class GithubOAuth2UserAccount
  extends OAuth2UserAccount {
  protected GithubOAuth2UserAccount(String email) {
    super(email, AccountType.GITHUB);
  }
}