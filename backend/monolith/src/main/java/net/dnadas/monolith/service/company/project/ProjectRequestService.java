package net.dnadas.monolith.service.company.project;

import lombok.RequiredArgsConstructor;
import net.dnadas.monolith.dto.requests.ProjectJoinRequestResponseDto;
import net.dnadas.monolith.dto.requests.ProjectJoinRequestUpdateDto;
import net.dnadas.monolith.exception.company.project.DuplicateProjectJoinRequestException;
import net.dnadas.monolith.exception.company.project.ProjectJoinRequestNotFoundException;
import net.dnadas.monolith.exception.company.project.ProjectNotFoundException;
import net.dnadas.monolith.exception.company.project.UserAlreadyInProjectException;
import net.dnadas.monolith.model.company.project.Project;
import net.dnadas.monolith.model.company.project.ProjectDao;
import net.dnadas.monolith.model.request.ProjectJoinRequest;
import net.dnadas.monolith.model.request.ProjectJoinRequestDao;
import net.dnadas.monolith.model.request.RequestStatus;
import net.dnadas.monolith.auth.model.user.ApplicationUser;
import net.dnadas.monolith.auth.service.user.UserProvider;
import net.dnadas.monolith.service.converter.ProjectConverter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectRequestService {
  private final ProjectDao projectDao;
  private final ProjectJoinRequestDao requestDao;
  private final ProjectRoleService projectRoleService;
  private final UserProvider userProvider;
  private final ProjectConverter projectConverter;

  public List<ProjectJoinRequestResponseDto> getOwnJoinRequests() {
    ApplicationUser applicationUser = userProvider.getAuthenticatedUser();
    List<ProjectJoinRequest> requests = requestDao.findByApplicationUser(applicationUser);
    return projectConverter.getProjectJoinRequestResponseDtos(requests);
  }

  @Transactional(rollbackFor = Exception.class)
  public ProjectJoinRequestResponseDto createJoinRequest(Long companyId, Long projectId) {
    ApplicationUser applicationUser = userProvider.getAuthenticatedUser();
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    if (project.getAssignedEmployees().contains(applicationUser)) {
      throw new UserAlreadyInProjectException();
    }
    Optional<ProjectJoinRequest> duplicateRequest = requestDao.findOneByProjectAndApplicationUser(
      project, applicationUser);
    if (duplicateRequest.isPresent()) {
      throw new DuplicateProjectJoinRequestException();
    }
    ProjectJoinRequest savedRequest = requestDao.save(
      new ProjectJoinRequest(project, applicationUser));
    return projectConverter.getProjectJoinRequestResponseDto(savedRequest);
  }

  @Transactional(rollbackFor = Exception.class)
  public void deleteOwnJoinRequestById(Long requestId) {
    ApplicationUser applicationUser = userProvider.getAuthenticatedUser();
    ProjectJoinRequest joinRequest = requestDao.findByIdAndApplicationUser(
      requestId,
      applicationUser).orElseThrow(() -> new ProjectJoinRequestNotFoundException(requestId));
    requestDao.delete(joinRequest);
  }

  @Transactional
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ASSIGNED_EMPLOYEE')")
  public List<ProjectJoinRequestResponseDto> getJoinRequestsOfProject(
    Long companyId, Long projectId) {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    List<ProjectJoinRequest> requests = requestDao.findByProjectAndStatus(
      project,
      RequestStatus.PENDING);
    return projectConverter.getProjectJoinRequestResponseDtos(requests);
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ASSIGNED_EMPLOYEE')")
  public void handleJoinRequest(
    Long companyId, Long projectId, Long requestId, ProjectJoinRequestUpdateDto updateDto) {
    ProjectJoinRequest request = requestDao.findByCompanyIdAndProjectIdAndRequestId(
      companyId, projectId, requestId).orElseThrow(
      () -> new ProjectJoinRequestNotFoundException(requestId));
    request.setStatus(updateDto.status());
    if (request.getStatus().equals(RequestStatus.APPROVED)) {
      projectRoleService.assignEmployee(companyId, projectId, request.getApplicationUser().getId());
      requestDao.delete(request);
    }
  }
}
