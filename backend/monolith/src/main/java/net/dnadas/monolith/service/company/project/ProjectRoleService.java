package net.dnadas.monolith.service.company.project;

import lombok.RequiredArgsConstructor;
import net.dnadas.monolith.auth.dto.user.UserResponsePublicDto;
import net.dnadas.monolith.exception.company.project.ProjectNotFoundException;
import net.dnadas.monolith.auth.exception.user.UserNotFoundException;
import net.dnadas.monolith.auth.model.authorization.PermissionType;
import net.dnadas.monolith.model.company.project.Project;
import net.dnadas.monolith.model.company.project.ProjectDao;
import net.dnadas.monolith.auth.model.user.ApplicationUser;
import net.dnadas.monolith.auth.model.user.ApplicationUserDao;
import net.dnadas.monolith.auth.model.user.GlobalRole;
import net.dnadas.monolith.auth.service.authorization.CustomPermissionEvaluator;
import net.dnadas.monolith.auth.service.user.UserProvider;
import net.dnadas.monolith.auth.service.user.UserConverter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProjectRoleService {
  private final ProjectDao projectDao;
  private final ApplicationUserDao applicationUserDao;
  private final UserConverter userConverter;
  private final UserProvider userProvider;
  private final CustomPermissionEvaluator permissionEvaluator;

  @Transactional(readOnly = true)
  public Set<PermissionType> getUserPermissionsForProject(Long companyId, Long projectId) {
    ApplicationUser user = userProvider.getAuthenticatedUser();
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));

    if (user.getGlobalRoles().contains(GlobalRole.ADMIN)) {
      return Set.of(PermissionType.PROJECT_ASSIGNED_EMPLOYEE, PermissionType.PROJECT_EDITOR,
        PermissionType.PROJECT_ADMIN);
    }

    Set<PermissionType> permissions = new HashSet<>();
    permissions.add(PermissionType.PROJECT_ASSIGNED_EMPLOYEE);
    if (permissionEvaluator.hasProjectEditorAccess(user.getId(), project)) {
      permissions.add(PermissionType.PROJECT_EDITOR);
    }
    if (permissionEvaluator.hasProjectAdminAccess(user.getId(), project)) {
      permissions.add(PermissionType.PROJECT_ADMIN);
    }
    return permissions;
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ADMIN')")
  public List<UserResponsePublicDto> getAssignedEmployees(Long companyId, Long projectId) {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    return userConverter.getUserResponsePublicDtos(
      project.getAssignedEmployees().stream().toList());
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ADMIN')")
  public void assignEmployee(Long companyId, Long projectId, Long userId) {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    ApplicationUser applicationUser = applicationUserDao.findById(userId).orElseThrow(
      () -> new UserNotFoundException(userId));
    project.assignEmployee(applicationUser);
    projectDao.save(project);
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ADMIN')")
  public void removeAssignedEmployee(Long companyId, Long projectId, Long userId) {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    ApplicationUser applicationUser = applicationUserDao.findById(userId).orElseThrow(
      () -> new UserNotFoundException(userId));
    project.removeEmployee(applicationUser);
    projectDao.save(project);
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ADMIN')")
  public List<UserResponsePublicDto> getEditors(Long companyId, Long projectId) {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    return userConverter.getUserResponsePublicDtos(project.getEditors().stream().toList());
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ADMIN')")
  public void addEditor(Long companyId, Long projectId, Long userId) {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    ApplicationUser applicationUser = applicationUserDao.findById(userId).orElseThrow(
      () -> new UserNotFoundException(userId));
    project.addEditor(applicationUser);
    projectDao.save(project);
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ADMIN')")
  public void removeEditor(Long companyId, Long projectId, Long userId) {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    ApplicationUser applicationUser = applicationUserDao.findById(userId).orElseThrow(
      () -> new UserNotFoundException(userId));
    project.removeEditor(applicationUser);
    projectDao.save(project);
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ADMIN')")
  public List<UserResponsePublicDto> getAdmins(Long companyId, Long projectId) {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    return userConverter.getUserResponsePublicDtos(project.getAdmins().stream().toList());
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ADMIN')")
  public void addAdmin(Long companyId, Long projectId, Long userId) {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    ApplicationUser applicationUser = applicationUserDao.findById(userId).orElseThrow(
      () -> new UserNotFoundException(userId));
    project.addAdmin(applicationUser);
    projectDao.save(project);
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ADMIN')")
  public void removeAdmin(Long companyId, Long projectId, Long userId) {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    ApplicationUser applicationUser = applicationUserDao.findById(userId).orElseThrow(
      () -> new UserNotFoundException(userId));
    project.removeAdmin(applicationUser);
    projectDao.save(project);
  }
}
