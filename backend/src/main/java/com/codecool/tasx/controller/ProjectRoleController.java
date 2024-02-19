package com.codecool.tasx.controller;

import com.codecool.tasx.dto.user.UserResponsePublicDto;
import com.codecool.tasx.service.company.project.ProjectRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies/{companyId}/projects/{projectId}")
public class ProjectRoleController {

  private final ProjectRoleService projectRoleService;

  @GetMapping("employees")
  public ResponseEntity<?> getEmployees(
    @PathVariable Long companyId, @PathVariable Long projectId) {
    List<UserResponsePublicDto> employees = projectRoleService.getAssignedEmployees(
      companyId,
      projectId);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", employees));
  }

  @PostMapping("employees")
  public ResponseEntity<?> addEmployee(
    @PathVariable Long companyId, @PathVariable Long projectId,
    @RequestParam(name = "userId") Long userId) {
    projectRoleService.assignEmployee(companyId, projectId, userId);
    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Employee added successfully"));
  }

  @DeleteMapping("employees/{userId}")
  public ResponseEntity<?> removeEmployee(
    @PathVariable Long companyId, @PathVariable Long projectId, @PathVariable Long userId) {
    projectRoleService.removeAssignedEmployee(companyId, projectId, userId);
    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Employee removed successfully"));
  }

  @GetMapping("editors")
  public ResponseEntity<?> getEditors(@PathVariable Long companyId, @PathVariable Long projectId) {
    List<UserResponsePublicDto> editors = projectRoleService.getEditors(companyId, projectId);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", editors));
  }

  @PostMapping("editors")
  public ResponseEntity<?> addEditor(
    @PathVariable Long companyId, @PathVariable Long projectId,
    @RequestParam(name = "userId") Long userId) {
    projectRoleService.addEditor(companyId, projectId, userId);
    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Editor added successfully"));
  }

  @DeleteMapping("editors/{userId}")
  public ResponseEntity<?> removeEditor(
    @PathVariable Long companyId, @PathVariable Long projectId, @PathVariable Long userId) {
    projectRoleService.removeEditor(companyId, projectId, userId);
    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Editor removed successfully"));
  }

  @GetMapping("admins")
  public ResponseEntity<?> getAdmins(@PathVariable Long companyId, @PathVariable Long projectId) {
    List<UserResponsePublicDto> admins = projectRoleService.getAdmins(companyId, projectId);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", admins));
  }

  @PostMapping("admins")
  public ResponseEntity<?> addAdmin(
    @PathVariable Long companyId, @PathVariable Long projectId,
    @RequestParam(name = "userId") Long userId) {
    projectRoleService.addAdmin(companyId, projectId, userId);
    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Admin added successfully"));
  }

  @DeleteMapping("admins/{userId}")
  public ResponseEntity<?> removeAdmin(
    @PathVariable Long companyId, @PathVariable Long projectId, @PathVariable Long userId) {
    projectRoleService.removeAdmin(companyId, projectId, userId);
    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Admin removed successfully"));
  }
}