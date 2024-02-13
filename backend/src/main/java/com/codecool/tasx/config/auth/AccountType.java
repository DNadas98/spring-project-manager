package com.codecool.tasx.config.auth;

public enum AccountType {
  LOCAL("Local", null),
  GOOGLE("Google", "google"),
  GITHUB("GitHub", "github");

  private final String displayName;
  private final String providerId;

  AccountType(String displayName, String providerId) {
    this.displayName = displayName;
    this.providerId = providerId;
  }

  public String getProviderId() {
    return providerId;
  }

  public String getDisplayName() {
    return displayName;
  }

}
