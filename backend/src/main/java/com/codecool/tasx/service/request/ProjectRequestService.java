package com.codecool.tasx.service.request;

import com.codecool.tasx.controller.dto.requests.ProjectJoinRequestResponseDto;
import com.codecool.tasx.controller.dto.requests.ProjectJoinRequestUpdateDto;
import com.codecool.tasx.exception.company.project.DuplicateProjectJoinRequestException;
import com.codecool.tasx.exception.company.project.ProjectJoinRequestNotFoundException;
import com.codecool.tasx.exception.company.project.ProjectNotFoundException;
import com.codecool.tasx.exception.company.project.UserAlreadyInProjectException;
import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.company.project.ProjectDao;
import com.codecool.tasx.model.requests.ProjectJoinRequest;
import com.codecool.tasx.model.requests.ProjectJoinRequestDao;
import com.codecool.tasx.model.requests.RequestStatus;
import com.codecool.tasx.model.user.ApplicationUser;
import com.codecool.tasx.service.auth.UserProvider;
import com.codecool.tasx.service.converter.ProjectConverter;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectRequestService {
  private final ProjectDao projectDao;
  private final ProjectJoinRequestDao requestDao;
  private final UserProvider userProvider;
  private final ProjectConverter projectConverter;
  private final Logger logger;

  @Autowired
  public ProjectRequestService(
    ProjectDao projectDao, ProjectJoinRequestDao requestDao, UserProvider userProvider,
    ProjectConverter projectConverter) {
    this.projectDao = projectDao;
    this.requestDao = requestDao;
    this.userProvider = userProvider;
    this.projectConverter = projectConverter;
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  @Transactional
  public ProjectJoinRequestResponseDto createJoinRequest(Long companyId, Long projectId) {
    ApplicationUser applicationUser = userProvider.getAuthenticatedUser();
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    if (project.getAssignedEmployees().contains(applicationUser)) {
      throw new UserAlreadyInProjectException();
    }
    Optional<ProjectJoinRequest> duplicateRequest = requestDao.findOneByProjectAndApplicationUser(
      project,
      applicationUser);
    if (duplicateRequest.isPresent()) {
      throw new DuplicateProjectJoinRequestException();
    }
    ProjectJoinRequest savedRequest = requestDao.save(new ProjectJoinRequest(project,
      applicationUser));
    return projectConverter.getProjectJoinRequestResponseDto(savedRequest);
  }

  @Transactional
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_EDITOR')")
  public List<ProjectJoinRequestResponseDto> getJoinRequestsOfProject(
    Long companyId, Long projectId) {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    List<ProjectJoinRequest> requests = requestDao.findByProjectAndStatus(
      project,
      RequestStatus.PENDING);
    return projectConverter.getProjectJoinRequestResponseDtos(requests);
  }

  @Transactional
  public List<ProjectJoinRequestResponseDto> getJoinRequestsOfUser() {
    ApplicationUser applicationUser = userProvider.getAuthenticatedUser();
    List<ProjectJoinRequest> requests = requestDao.findByApplicationUser(applicationUser);
    return projectConverter.getProjectJoinRequestResponseDtos(requests);
  }

  @Transactional(rollbackOn = Exception.class)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ASSIGNED_EMPLOYEE')")
  public void handleJoinRequest(
    Long companyId, Long projectId, Long requestId, ProjectJoinRequestUpdateDto updateDto) {
    ProjectJoinRequest request = requestDao.findByCompanyIdAndProjectIdAndRequestId(
      companyId, projectId, requestId).orElseThrow(
      () -> new ProjectJoinRequestNotFoundException(requestId));
    request.setStatus(updateDto.status());
    if (request.getStatus().equals(RequestStatus.APPROVED)) {
      addUserToProject(request.getUser(), request.getProject());
      requestDao.delete(request);
    }
  }

  private void addUserToProject(ApplicationUser applicationUser, Project project) {
    project.assignEmployee(applicationUser);
  }
}
