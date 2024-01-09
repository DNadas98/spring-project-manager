package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.requests.ProjectJoinRequestResponseDto;
import com.codecool.tasx.controller.dto.requests.ProjectJoinRequestUpdateDto;
import com.codecool.tasx.service.request.ProjectRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/companies/{companyId}/projects/{projectId}/requests")
public class ProjectRequestController {
  private final ProjectRequestService projectJoinRequestService;
  private final Logger logger;

  @Autowired
  public ProjectRequestController(ProjectRequestService projectJoinRequestService) {
    this.projectJoinRequestService = projectJoinRequestService;
    logger = LoggerFactory.getLogger(this.getClass());
  }

  @GetMapping()
  public ResponseEntity<?> readJoinRequestsOfProject(
    @PathVariable Long companyId, @PathVariable Long projectId) {

    List<ProjectJoinRequestResponseDto> requests =
      projectJoinRequestService.getJoinRequestsOfProject(companyId, projectId);

    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", requests));
  }

  @PostMapping()
  public ResponseEntity<?> joinProject(@PathVariable Long companyId, @PathVariable Long projectId) {
    ProjectJoinRequestResponseDto createdRequest = projectJoinRequestService.createJoinRequest(
      companyId, projectId);

    return ResponseEntity.status(HttpStatus.CREATED).body(
      Map.of("message", "Request created successfully", "data", createdRequest));
  }

  @PutMapping("/{requestId}")
  public ResponseEntity<?> updateJoinRequestById(
    @PathVariable Long requestId, @RequestBody ProjectJoinRequestUpdateDto requestDto,
    @PathVariable Long companyId, @PathVariable Long projectId) {

    projectJoinRequestService.handleJoinRequest(companyId, projectId, requestId, requestDto);

    //TODO: notify the user who requested to join...
    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Request updated successfully"));
  }
}
