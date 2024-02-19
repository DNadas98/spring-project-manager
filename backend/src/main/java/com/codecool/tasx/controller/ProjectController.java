package com.codecool.tasx.controller;

import com.codecool.tasx.dto.company.project.ProjectCreateRequestDto;
import com.codecool.tasx.dto.company.project.ProjectResponsePrivateDTO;
import com.codecool.tasx.dto.company.project.ProjectResponsePublicDTO;
import com.codecool.tasx.dto.company.project.ProjectUpdateRequestDto;
import com.codecool.tasx.service.company.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/companies/{companyId}/projects")
@RequiredArgsConstructor
public class ProjectController {
  private final ProjectService projectService;

  @GetMapping()
  public ResponseEntity<?> getProjectsWithUser(
    @PathVariable Long companyId, @RequestParam(name = "withUser") Boolean withUser) {
    List<ProjectResponsePublicDTO> projects;
    if (withUser) {
      projects = projectService.getProjectsWithUser(companyId);
    } else {
      projects = projectService.getProjectsWithoutUser(companyId);
    }
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", projects));
  }

  @GetMapping("/{projectId}")
  public ResponseEntity<?> getProjectById(
    @PathVariable Long companyId, @PathVariable Long projectId) {
    ProjectResponsePrivateDTO project = projectService.getProjectById(companyId, projectId);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", project));
  }

  @PostMapping
  public ResponseEntity<?> createProject(
    @PathVariable Long companyId, @RequestBody ProjectCreateRequestDto projectDetails) {
    ProjectResponsePrivateDTO projectResponseDetails = projectService.createProject(
      projectDetails, companyId);
    return ResponseEntity.status(HttpStatus.CREATED).body(
      Map.of("message", "Project created successfully", "data", projectResponseDetails));
  }

  @PutMapping("/{projectId}")
  public ResponseEntity<?> updateProject(
    @PathVariable Long companyId, @PathVariable Long projectId,
    @RequestBody ProjectUpdateRequestDto projectDetails) {
    ProjectResponsePrivateDTO projectResponseDetails = projectService.updateProject(
      projectDetails, companyId, projectId);

    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Project with ID " + projectId + " updated successfully", "data",
        projectResponseDetails));
  }

  @DeleteMapping("/{projectId}")
  public ResponseEntity<?> deleteProject(
    @PathVariable Long companyId, @PathVariable Long projectId) {
    projectService.deleteProject(companyId, projectId);

    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Project with ID " + projectId + " deleted successfully"));
  }
}
