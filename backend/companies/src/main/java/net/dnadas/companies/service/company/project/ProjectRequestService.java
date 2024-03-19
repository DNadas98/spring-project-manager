package net.dnadas.companies.service.company.project;

import lombok.RequiredArgsConstructor;
import net.dnadas.auth.dto.authentication.UserInfoDto;
import net.dnadas.auth.dto.user.UserResponsePublicDto;
import net.dnadas.companies.dto.requests.ProjectJoinRequestResponseDto;
import net.dnadas.companies.dto.requests.ProjectJoinRequestUpdateDto;
import net.dnadas.companies.exception.company.project.DuplicateProjectJoinRequestException;
import net.dnadas.companies.exception.company.project.ProjectJoinRequestNotFoundException;
import net.dnadas.companies.exception.company.project.ProjectNotFoundException;
import net.dnadas.companies.exception.company.project.UserAlreadyInProjectException;
import net.dnadas.companies.model.company.project.Project;
import net.dnadas.companies.model.company.project.ProjectDao;
import net.dnadas.companies.model.request.ProjectJoinRequest;
import net.dnadas.companies.model.request.ProjectJoinRequestDao;
import net.dnadas.companies.model.request.RequestStatus;
import net.dnadas.companies.service.converter.ProjectConverter;
import net.dnadas.companies.service.user.UserProvider;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectRequestService {
  private final ProjectDao projectDao;
  private final ProjectJoinRequestDao requestDao;
  private final ProjectRoleService projectRoleService;
  private final UserProvider userProvider;
  private final ProjectConverter projectConverter;

  public List<ProjectJoinRequestResponseDto> getOwnJoinRequests() {
    UserInfoDto userInfo = userProvider.getUserInfo();
    List<ProjectJoinRequest> requests = requestDao.findByUserId(userInfo.userId());
    List<ProjectJoinRequestResponseDto> requestResponseDtos = requests.stream().map(request ->
        new ProjectJoinRequestResponseDto(request.getId(),
          projectConverter.getProjectResponsePublicDto(request.getProject()),
          new UserResponsePublicDto(request.getUserId(), userInfo.username()), request.getStatus()))
      .collect(
        Collectors.toList());
    return requestResponseDtos;
  }

  @Transactional(rollbackFor = Exception.class)
  public ProjectJoinRequestResponseDto createJoinRequest(Long companyId, Long projectId) {
    UserInfoDto userInfo = userProvider.getUserInfo();
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    if (project.getAssignedEmployees().contains(userInfo.userId())) {
      throw new UserAlreadyInProjectException();
    }
    Optional<ProjectJoinRequest> duplicateRequest = requestDao.findByProjectAndUserId(
      project, userInfo.userId());
    if (duplicateRequest.isPresent()) {
      throw new DuplicateProjectJoinRequestException();
    }
    ProjectJoinRequest savedRequest = requestDao.save(new ProjectJoinRequest(
      project,
      userInfo.userId()));
    return new ProjectJoinRequestResponseDto(
      savedRequest.getId(),
      projectConverter.getProjectResponsePublicDto(savedRequest.getProject()),
      new UserResponsePublicDto(savedRequest.getUserId(), userInfo.username()),
      savedRequest.getStatus());
  }

  @Transactional(rollbackFor = Exception.class)
  public void deleteOwnJoinRequestById(Long requestId) {
    Long userId = userProvider.getAuthenticatedUserId();
    ProjectJoinRequest joinRequest = requestDao.findByIdAndUserId(requestId, userId).orElseThrow(
      () -> new ProjectJoinRequestNotFoundException(requestId));
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
    //TODO: implement
    List<ProjectJoinRequestResponseDto> requestResponseDtos = requests.stream().map(request ->
      new ProjectJoinRequestResponseDto(request.getId(),
        projectConverter.getProjectResponsePublicDto(request.getProject()),
        new UserResponsePublicDto(request.getUserId(), "username"), request.getStatus())).collect(
      Collectors.toList());
    return requestResponseDtos;
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
      projectRoleService.assignEmployee(companyId, projectId, request.getUserId());
      requestDao.delete(request);
    }
  }
}
