package com.codecool.tasx.model.verification;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
public abstract class VerificationToken {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @CreationTimestamp
  private LocalDateTime createdAt;
  private String verificationCodeHash;
  @Column(nullable = false)
  private TokenType tokenType;

  protected VerificationToken() {
  }

  protected VerificationToken(TokenType tokenType, String verificationCodeHash) {
    this.tokenType = tokenType;
    this.verificationCodeHash = verificationCodeHash;
  }
}
