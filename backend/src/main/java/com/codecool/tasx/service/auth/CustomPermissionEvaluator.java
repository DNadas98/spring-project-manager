package com.codecool.tasx.service.auth;

import com.codecool.tasx.config.auth.SecurityConfig;
import com.codecool.tasx.exception.company.CompanyNotFoundException;
import com.codecool.tasx.exception.company.project.ProjectNotFoundException;
import com.codecool.tasx.exception.company.project.task.TaskNotFoundException;
import com.codecool.tasx.model.auth.PermissionType;
import com.codecool.tasx.model.auth.account.UserAccount;
import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.CompanyDao;
import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.company.project.ProjectDao;
import com.codecool.tasx.model.company.project.task.Task;
import com.codecool.tasx.model.company.project.task.TaskDao;
import com.codecool.tasx.model.user.ApplicationUser;
import com.codecool.tasx.model.user.GlobalRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @see SecurityConfig
 * @see org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
 * @see org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
 */
@Component
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {
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
  @Transactional
  public boolean hasPermission(
    Authentication authentication, Object targetDomainObject, Object permission) {
    if ((authentication == null) || (targetDomainObject == null) ||
      !(permission instanceof PermissionType)) {
      return false;
    }
    ApplicationUser applicationUser = (ApplicationUser) authentication.getPrincipal();

    if (applicationUser.getGlobalRoles().contains(GlobalRole.ADMIN)) {
      return true;
    }

    if (targetDomainObject instanceof Company) {
      return handleCompanyPermissions(
        applicationUser, (Company) targetDomainObject, (PermissionType) permission);
    } else if (targetDomainObject instanceof Project) {
      return handleProjectPermissions(
        applicationUser, (Project) targetDomainObject, (PermissionType) permission);
    } else if (targetDomainObject instanceof Task) {
      return handleTaskPermissions(
        applicationUser, (Task) targetDomainObject, (PermissionType) permission);
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
  @Transactional
  public boolean hasPermission(
    Authentication authentication, Serializable targetId, String targetType, Object permission) {
    if ((authentication == null) || (targetId == null) || (targetType.isEmpty()) ||
      (permission == null)) {
      return false;
    }
    UserAccount account = (UserAccount) authentication.getPrincipal();
    ApplicationUser applicationUser = account.getApplicationUser();

    if (applicationUser.getGlobalRoles().contains(GlobalRole.ADMIN)) {
      return true;
    }

    Long id = (Long) targetId;
    PermissionType permissionType = PermissionType.valueOf(permission.toString());

    switch (targetType) {
      case "Company" -> {
        Company company = companyDao.findById(id).orElseThrow(
          () -> new CompanyNotFoundException(id));
        return handleCompanyPermissions(applicationUser, company, permissionType);
      }
      case "Project" -> {
        Project project = projectDao.findById(id).orElseThrow(
          () -> new ProjectNotFoundException(id));
        return handleProjectPermissions(applicationUser, project, permissionType);
      }
      case "Task" -> {
        Task task = taskDao.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        return handleTaskPermissions(applicationUser, task, permissionType);
      }
      default -> {
        return false;
      }
    }
  }

  private boolean handleCompanyPermissions(
    ApplicationUser applicationUser, Company company, PermissionType permissionType) {
    switch (permissionType) {
      case COMPANY_ADMIN:
        return hasCompanyAdminAccess(applicationUser, company);
      case COMPANY_EDITOR:
        return hasCompanyEditorAccess(applicationUser, company);
      case COMPANY_EMPLOYEE:
        return hasCompanyEmployeeAccess(applicationUser, company);
      default:
        return false;
    }
  }

  private boolean handleProjectPermissions(
    ApplicationUser applicationUser, Project project, PermissionType permissionType) {
    switch (permissionType) {
      case PROJECT_ADMIN:
        return hasProjectAdminAccess(applicationUser, project);
      case PROJECT_EDITOR:
        return hasProjectEditorAccess(applicationUser, project);
      case PROJECT_ASSIGNED_EMPLOYEE:
        return hasProjectAssignedEmployeeAccess(applicationUser, project);
      default:
        return false;
    }
  }

  private boolean handleTaskPermissions(
    ApplicationUser applicationUser, Task task, PermissionType permissionType) {
    switch (permissionType) {
      case TASK_ASSIGNED_EMPLOYEE:
        return hasTaskAssignedEmployeeAccess(applicationUser, task);
      default:
        return false;
    }
  }

  @Transactional
  public boolean hasCompanyAdminAccess(ApplicationUser applicationUser, Company company) {
    return applicationUser.getAdminCompanies().contains(company);
  }

  @Transactional
  public boolean hasCompanyEditorAccess(ApplicationUser applicationUser, Company company) {
    return applicationUser.getEditorCompanies().contains(company);
  }

  @Transactional
  public boolean hasCompanyEmployeeAccess(ApplicationUser applicationUser, Company company) {
    return applicationUser.getEmployeeCompanies().contains(company) || hasCompanyEditorAccess(
      applicationUser, company) || hasCompanyAdminAccess(applicationUser, company);
  }

  @Transactional
  public boolean hasProjectAdminAccess(ApplicationUser applicationUser, Project project) {
    return project.getAdmins().contains(applicationUser) || hasCompanyAdminAccess(
      applicationUser,
      project.getCompany());
  }

  @Transactional
  public boolean hasProjectEditorAccess(ApplicationUser applicationUser, Project project) {
    return project.getEditors().contains(applicationUser) || hasCompanyEditorAccess(
      applicationUser,
      project.getCompany());
  }

  @Transactional
  public boolean hasProjectAssignedEmployeeAccess(
    ApplicationUser applicationUser, Project project) {
    return project.getAssignedEmployees().contains(applicationUser) || hasProjectEditorAccess(
      applicationUser, project) || hasProjectAdminAccess(applicationUser, project);
  }

  @Transactional
  public boolean hasTaskAssignedEmployeeAccess(ApplicationUser applicationUser, Task task) {
    return task.getAssignedEmployees().contains(applicationUser) ||
      hasProjectAssignedEmployeeAccess(applicationUser, task.getProject());
  }
}
