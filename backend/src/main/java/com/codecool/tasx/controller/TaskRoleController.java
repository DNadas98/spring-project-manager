package com.codecool.tasx.controller;

import com.codecool.tasx.dto.user.UserResponsePublicDto;
import com.codecool.tasx.service.company.project.task.TaskRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies/{companyId}/projects/{projectId}/tasks/{taskId}")
public class TaskRoleController {


  private final TaskRoleService taskRoleService;

  @GetMapping("employees")
  public ResponseEntity<?> getEmployees(
    @PathVariable Long companyId, @PathVariable Long projectId, @PathVariable Long taskId) {
    List<UserResponsePublicDto> employees = taskRoleService.getAssignedEmployees(
      companyId,
      projectId,
      taskId);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", employees));
  }

  @PostMapping("employees")
  public ResponseEntity<?> addSelf(
    @PathVariable Long companyId, @PathVariable Long projectId, @PathVariable Long taskId) {
    taskRoleService.assignSelf(companyId, projectId, taskId);
    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Employee added successfully"));
  }

  @DeleteMapping("employees")
  public ResponseEntity<?> removeSelf(
    @PathVariable Long companyId, @PathVariable Long projectId, @PathVariable Long taskId) {
    taskRoleService.removeSelf(companyId, projectId, taskId);
    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Employee removed successfully"));
  }
}