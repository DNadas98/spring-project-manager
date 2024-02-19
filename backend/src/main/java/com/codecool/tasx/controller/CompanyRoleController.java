package com.codecool.tasx.controller;

import com.codecool.tasx.dto.user.UserResponsePublicDto;
import com.codecool.tasx.service.company.CompanyRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies/{companyId}")
public class CompanyRoleController {
  private final CompanyRoleService companyRoleService;

  @GetMapping("employees")
  public ResponseEntity<?> getEmployees(@PathVariable Long companyId) {
    List<UserResponsePublicDto> employees = companyRoleService.getEmployees(companyId);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", employees));
  }

  @PostMapping("employees")
  public ResponseEntity<?> addEmployee(
    @PathVariable Long companyId, @RequestParam(name = "userId") Long userId) {
    companyRoleService.addEmployee(companyId, userId);
    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Employee added successfully"));
  }

  @DeleteMapping("employees/{userId}")
  public ResponseEntity<?> removeEmployee(@PathVariable Long companyId, @PathVariable Long userId) {
    companyRoleService.removeEmployee(companyId, userId);
    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Employee removed successfully"));
  }

  @GetMapping("editors")
  public ResponseEntity<?> getEditors(@PathVariable Long companyId) {
    List<UserResponsePublicDto> editors = companyRoleService.getEditors(companyId);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", editors));
  }

  @PostMapping("editors")
  public ResponseEntity<?> addEditor(
    @PathVariable Long companyId, @RequestParam(name = "userId") Long userId) {
    companyRoleService.addEditor(companyId, userId);
    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Editor added successfully"));
  }

  @DeleteMapping("editors/{userId}")
  public ResponseEntity<?> removeEditor(@PathVariable Long companyId, @PathVariable Long userId) {
    companyRoleService.removeEditor(companyId, userId);
    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Editor removed successfully"));
  }

  @GetMapping("admins")
  public ResponseEntity<?> getAdmins(@PathVariable Long companyId) {
    List<UserResponsePublicDto> admins = companyRoleService.getAdmins(companyId);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", admins));
  }

  @PostMapping("admins")
  public ResponseEntity<?> addAdmin(
    @PathVariable Long companyId, @RequestParam(name = "userId") Long userId) {
    companyRoleService.addAdmin(companyId, userId);
    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Admin added successfully"));
  }

  @DeleteMapping("admins/{userId}")
  public ResponseEntity<?> removeAdmin(@PathVariable Long companyId, @PathVariable Long userId) {
    companyRoleService.removeAdmin(companyId, userId);
    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Admin removed successfully"));
  }
}