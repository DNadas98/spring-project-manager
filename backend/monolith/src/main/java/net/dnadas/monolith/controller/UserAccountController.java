package net.dnadas.monolith.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import net.dnadas.monolith.dto.auth.UserAccountResponseDto;
import net.dnadas.monolith.service.auth.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/api/v1/user/accounts")
@RequiredArgsConstructor
public class UserAccountController {
  private final UserAccountService accountService;

  @GetMapping
  public ResponseEntity<?> getOwnUserAccounts() {
    Set<UserAccountResponseDto> accounts = accountService.findAllOfApplicationUser();
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", accounts));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteOwnUserAccount(@PathVariable @Min(1) Long id) {
    accountService.deleteOwnUserAccountById(id);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of(
      "message",
      "User account deleted successfully"));
  }
}
