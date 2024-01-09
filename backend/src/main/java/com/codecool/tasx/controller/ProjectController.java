package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.company.project.ProjectCreateRequestDto;
import com.codecool.tasx.controller.dto.company.project.ProjectResponsePrivateDTO;
import com.codecool.tasx.controller.dto.company.project.ProjectResponsePublicDTO;
import com.codecool.tasx.controller.dto.company.project.ProjectUpdateRequestDto;
import com.codecool.tasx.service.company.project.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/companies/{companyId}/projects")
public class ProjectController {
  private final ProjectService projectService;
  private final Logger logger;

  @Autowired
  public ProjectController(ProjectService projectService) {
    this.projectService = projectService;
    logger = LoggerFactory.getLogger(this.getClass());
  }

  @GetMapping("/withoutUser")
  public ResponseEntity<?> getProjectsWithoutUser(
    @PathVariable Long companyId) {
    List<ProjectResponsePublicDTO> projects = projectService.getProjectsWithoutUser(companyId);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", projects));
  }

  @GetMapping("/withUser")
  public ResponseEntity<?> getProjectsWithUser(
    @PathVariable Long companyId) {
    List<ProjectResponsePublicDTO> projects = projectService.getProjectsWithUser(companyId);
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
