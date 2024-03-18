package net.dnadas.monolith.service.company.project;

import lombok.RequiredArgsConstructor;
import net.dnadas.auth.dto.user.UserResponsePublicDto;
import net.dnadas.monolith.exception.company.project.ProjectNotFoundException;
import net.dnadas.monolith.model.authorization.PermissionType;
import net.dnadas.monolith.model.company.project.Project;
import net.dnadas.monolith.model.company.project.ProjectDao;
import net.dnadas.monolith.service.authorization.CustomPermissionEvaluator;
import net.dnadas.monolith.service.user.UserProvider;
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
  private final UserProvider userProvider;
  private final CustomPermissionEvaluator permissionEvaluator;

  @Transactional(readOnly = true)
  public Set<PermissionType> getUserPermissionsForProject(Long companyId, Long projectId) {
    Long userId = userProvider.getAuthenticatedUserId();
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    Set<PermissionType> permissions = new HashSet<>();
    permissions.add(PermissionType.PROJECT_ASSIGNED_EMPLOYEE);
    if (permissionEvaluator.hasProjectEditorAccess(userId, project)) {
      permissions.add(PermissionType.PROJECT_EDITOR);
    }
    if (permissionEvaluator.hasProjectAdminAccess(userId, project)) {
      permissions.add(PermissionType.PROJECT_ADMIN);
    }
    return permissions;
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ADMIN')")
  public List<UserResponsePublicDto> getAssignedEmployees(Long companyId, Long projectId) {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    //TODO: implement
    return List.of();

  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ADMIN')")
  public void assignEmployee(Long companyId, Long projectId, Long userId) {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    project.assignEmployee(userId);
    projectDao.save(project);
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ADMIN')")
  public void removeAssignedEmployee(Long companyId, Long projectId, Long userId) {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    project.removeEmployee(userId);
    projectDao.save(project);
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ADMIN')")
  public List<UserResponsePublicDto> getEditors(Long companyId, Long projectId) {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    //TODO: implement
    return List.of();
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ADMIN')")
  public void addEditor(Long companyId, Long projectId, Long userId) {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    project.addEditor(userId);
    projectDao.save(project);
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ADMIN')")
  public void removeEditor(Long companyId, Long projectId, Long userId) {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    project.removeEditor(userId);
    projectDao.save(project);
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ADMIN')")
  public List<UserResponsePublicDto> getAdmins(Long companyId, Long projectId) {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    //TODO: implement
    return List.of();
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ADMIN')")
  public void addAdmin(Long companyId, Long projectId, Long userId) {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    project.addAdmin(userId);
    projectDao.save(project);
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ADMIN')")
  public void removeAdmin(Long companyId, Long projectId, Long userId) {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    project.removeAdmin(userId);
    projectDao.save(project);
  }
}
