package com.codecool.tasx.service.auth;

import com.codecool.tasx.config.auth.SecurityConfig;
import com.codecool.tasx.exception.company.CompanyNotFoundException;
import com.codecool.tasx.exception.company.project.ProjectNotFoundException;
import com.codecool.tasx.exception.company.project.task.TaskNotFoundException;
import com.codecool.tasx.exception.user.UserNotFoundException;
import com.codecool.tasx.model.auth.PermissionType;
import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.CompanyDao;
import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.company.project.ProjectDao;
import com.codecool.tasx.model.company.project.task.Task;
import com.codecool.tasx.model.company.project.task.TaskDao;
import com.codecool.tasx.model.user.ApplicationUser;
import com.codecool.tasx.model.user.ApplicationUserDao;
import com.codecool.tasx.model.user.GlobalRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Objects;

/**
 * @see SecurityConfig
 * @see org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
 * @see org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
 */
@Component
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {
  private final ApplicationUserDao applicationUserDao;
  private final CompanyDao companyDao;
  private final ProjectDao projectDao;
  private final TaskDao taskDao;

  /**
   * @param authentication     <strong>Passed in automatically</strong> from the {@link org.springframework.security.core.context.SecurityContextHolder}<br>
   *                           Represents the user in question. Should not be null.
   * @param targetDomainObject The domain object for which permissions should be
   *                           checked. May be null in which case implementations should return false, as the null
   *                           condition can be checked explicitly in the expression.
   * @param permission         A representation of the permission object as supplied by the
   *                           expression system. Not null.
   * @return true if the permission is granted, false otherwise
   */
  @Override
  @Transactional(readOnly = true)
  public boolean hasPermission(
    Authentication authentication, Object targetDomainObject, Object permission) {
    if ((authentication == null) || (targetDomainObject == null) ||
      !(permission instanceof PermissionType)) {
      return false;
    }

    Long userId = (Long) authentication.getPrincipal();
    if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(GlobalRole.ADMIN.name()))) {
      return true;
    }

    switch (targetDomainObject) {
      case Company company -> {
        return handleCompanyPermissions(
          userId, company, (PermissionType) permission);
      }
      case Project project -> {
        return handleProjectPermissions(
          userId, project, (PermissionType) permission);
      }
      case Task task -> {
        return handleTaskPermissions(
          userId, task, (PermissionType) permission);
      }
      default -> {
      }
    }
    return false;
  }

  /**
   * Alternative method for evaluating a permission where only the identifier of the
   * target object is available, rather than the target instance itself.
   *
   * @param authentication <strong>Passed in automatically</strong> from the {@link org.springframework.security.core.context.SecurityContextHolder}<br>
   *                       Represents the user in question. Should not be null.
   * @param targetId       The identifier for the object instance (usually a Long)
   * @param targetType     A String representing the target's type (usually a Java
   *                       classname). Not null.
   * @param permission     A representation of the permission object as supplied by the
   *                       expression system. Not null.
   * @return true if the permission is granted, false otherwise
   * @Warning Not type safe
   */
  @Override
  @Transactional(readOnly = true)
  public boolean hasPermission(
    Authentication authentication, Serializable targetId, String targetType, Object permission) {
    if ((authentication == null) || (targetId == null) || (targetType.isEmpty()) ||
      (permission == null)) {
      return false;
    }

    Long userId = (Long) authentication.getPrincipal();
    if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(GlobalRole.ADMIN.name()))) {
      return true;
    }

    Long id = (Long) targetId;
    PermissionType permissionType = PermissionType.valueOf(permission.toString());

    switch (targetType) {
      case "Company" -> {
        Company company = companyDao.findById(id).orElseThrow(
          () -> new CompanyNotFoundException(id));
        return handleCompanyPermissions(userId, company, permissionType);
      }
      case "Project" -> {
        Project project = projectDao.findById(id).orElseThrow(
          () -> new ProjectNotFoundException(id));
        return handleProjectPermissions(userId, project, permissionType);
      }
      case "Task" -> {
        Task task = taskDao.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        return handleTaskPermissions(userId, task, permissionType);
      }
      default -> {
        return false;
      }
    }
  }

  @Transactional(readOnly = true)
  public boolean handleCompanyPermissions(
    Long userId, Company company, PermissionType permissionType) {
    return switch (permissionType) {
      case COMPANY_ADMIN -> hasCompanyAdminAccess(userId, company);
      case COMPANY_EDITOR -> hasCompanyEditorAccess(userId, company);
      case COMPANY_EMPLOYEE -> hasCompanyEmployeeAccess(userId, company);
      default -> false;
    };
  }

  @Transactional(readOnly = true)
  public boolean handleProjectPermissions(
    Long userId, Project project, PermissionType permissionType) {
    return switch (permissionType) {
      case PROJECT_ADMIN -> hasProjectAdminAccess(userId, project);
      case PROJECT_EDITOR -> hasProjectEditorAccess(userId, project);
      case PROJECT_ASSIGNED_EMPLOYEE -> hasProjectAssignedEmployeeAccess(userId, project);
      default -> false;
    };
  }

  @Transactional(readOnly = true)
  public boolean handleTaskPermissions(
    Long userId, Task task, PermissionType permissionType) {
    if (Objects.requireNonNull(permissionType) == PermissionType.TASK_ASSIGNED_EMPLOYEE) {
      return hasTaskAssignedEmployeeAccess(userId, task);
    }
    return false;
  }

  @Transactional(readOnly = true)
  public boolean hasCompanyAdminAccess(Long userId, Company company) {
    ApplicationUser applicationUser = applicationUserDao.findByIdAndFetchAdminCompanies(userId)
      .orElseThrow(UserNotFoundException::new);
    return applicationUser.getGlobalRoles().contains(GlobalRole.ADMIN) ||
      applicationUser.getAdminCompanies().contains(company);
  }

  @Transactional(readOnly = true)
  public boolean hasCompanyEditorAccess(Long userId, Company company) {
    ApplicationUser applicationUser = applicationUserDao.findByIdAndFetchEditorCompanies(userId)
      .orElseThrow(UserNotFoundException::new);
    return applicationUser.getGlobalRoles().contains(GlobalRole.ADMIN) ||
      applicationUser.getEditorCompanies().contains(company);
  }

  @Transactional(readOnly = true)
  public boolean hasCompanyEmployeeAccess(Long userId, Company company) {
    ApplicationUser applicationUser = applicationUserDao.findByIdAndFetchEmployeeCompanies(userId)
      .orElseThrow(UserNotFoundException::new);
    return applicationUser.getGlobalRoles().contains(GlobalRole.ADMIN) ||
      applicationUser.getEmployeeCompanies().contains(company);
  }

  @Transactional(readOnly = true)
  public boolean hasProjectAdminAccess(Long userId, Project project) {
    ApplicationUser applicationUser = applicationUserDao.findByIdAndFetchAdminProjects(userId)
      .orElseThrow(UserNotFoundException::new);
    return applicationUser.getGlobalRoles().contains(GlobalRole.ADMIN) ||
      applicationUser.getAdminProjects().contains(project) || hasCompanyAdminAccess(
      userId,
      project.getCompany());
  }

  @Transactional(readOnly = true)
  public boolean hasProjectEditorAccess(Long userId, Project project) {
    ApplicationUser applicationUser = applicationUserDao.findByIdAndFetchEditorProjects(userId)
      .orElseThrow(UserNotFoundException::new);
    return applicationUser.getGlobalRoles().contains(GlobalRole.ADMIN) ||
      applicationUser.getEditorProjects().contains(project) || hasCompanyEditorAccess(
      userId,
      project.getCompany());
  }

  @Transactional(readOnly = true)
  public boolean hasProjectAssignedEmployeeAccess(
    Long userId, Project project) {
    ApplicationUser applicationUser = applicationUserDao.findByIdAndFetchAssignedProjects(userId)
      .orElseThrow(UserNotFoundException::new);
    return applicationUser.getGlobalRoles().contains(GlobalRole.ADMIN) ||
      applicationUser.getAssignedProjects().contains(project) || hasCompanyEditorAccess(
      userId,
      project.getCompany());
  }

  @Transactional(readOnly = true)
  public boolean hasTaskAssignedEmployeeAccess(
    Long userId, Task task) {
    ApplicationUser applicationUser = applicationUserDao.findByIdAndFetchAssignedTasks(userId)
      .orElseThrow(UserNotFoundException::new);
    return applicationUser.getGlobalRoles().contains(GlobalRole.ADMIN) ||
      applicationUser.getAssignedTasks().contains(task) || hasProjectEditorAccess(
      userId,
      task.getProject());
  }
}
