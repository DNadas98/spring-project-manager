package net.dnadas.companies.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import net.dnadas.companies.model.authorization.PermissionType;
import net.dnadas.companies.service.company.CompanyRoleService;
import net.dnadas.companies.service.company.project.ProjectRoleService;
import net.dnadas.companies.service.company.project.task.TaskRoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/api/v1/companies/permissions")
@RequiredArgsConstructor
public class PermissionController {
  private final CompanyRoleService companyRoleService;
  private final ProjectRoleService projectRoleService;
  private final TaskRoleService taskRoleService;

  @GetMapping("/{companyId}")
  public ResponseEntity<?> getOwnPermissionsForCompany(@PathVariable @Min(1) Long companyId) {
    Set<PermissionType> permissions = companyRoleService.getUserPermissionsForCompany(companyId);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", permissions));
  }

  @GetMapping("/{companyId}/projects/{projectId}")
  public ResponseEntity<?> getOwnPermissionsForProject(
    @PathVariable @Min(1) Long companyId, @PathVariable @Min(1) Long projectId
  ) {
    Set<PermissionType> permissions = projectRoleService.getUserPermissionsForProject(
      companyId, projectId);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", permissions));
  }

  @GetMapping("/{companyId}/projects/{projectId}/tasks/{taskId}")
  public ResponseEntity<?> getOwnPermissionsForTask(
    @PathVariable @Min(1) Long companyId, @PathVariable @Min(1) Long projectId,
    @PathVariable @Min(1) Long taskId
  ) {
    Set<PermissionType> permissions = taskRoleService.getUserPermissionsForTask(
      companyId, projectId, taskId);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", permissions));
  }
}
