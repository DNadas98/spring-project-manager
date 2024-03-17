package net.dnadas.monolith.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.dnadas.monolith.auth.dto.user.UserResponsePrivateDto;
import net.dnadas.monolith.auth.dto.user.UserUsernameUpdateDto;
import net.dnadas.monolith.auth.service.user.ApplicationUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
  private final ApplicationUserService applicationUserService;

  @GetMapping
  public ResponseEntity<?> getOwnApplicationUser() {
    UserResponsePrivateDto userDetails = applicationUserService.getOwnUserDetails();
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", userDetails));
  }

  @PatchMapping("/username")
  public ResponseEntity<?> updateOwnApplicationUser(
    @RequestBody @Valid UserUsernameUpdateDto updateDto) {
    UserResponsePrivateDto userDetails = applicationUserService.updateOwnUsername(
      updateDto.username());
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", userDetails));
  }

  @DeleteMapping
  public ResponseEntity<?> deleteOwnApplicationUser() {
    applicationUserService.deleteOwnApplicationUser();
    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Application user deleted successfully"));
  }
}
