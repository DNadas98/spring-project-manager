package com.codecool.tasx.model.auth.account;

import com.codecool.tasx.model.user.ApplicationUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
@Getter
@Setter
public abstract class UserAccount {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(unique = true, nullable = false)
  private String email;

  @Enumerated(EnumType.STRING)
  private AccountType accountType;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "application_user_id")
  private ApplicationUser applicationUser;

  protected UserAccount(String email, AccountType accountType) {
    this.email = email;
    this.accountType = accountType;
  }
}
