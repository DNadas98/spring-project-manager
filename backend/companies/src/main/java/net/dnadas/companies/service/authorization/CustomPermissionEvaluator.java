package net.dnadas.companies.service.authorization;

import lombok.RequiredArgsConstructor;
import net.dnadas.auth.dto.authentication.UserInfoDto;
import net.dnadas.auth.model.user.GlobalRole;
import net.dnadas.companies.config.SecurityConfig;
import net.dnadas.companies.exception.company.CompanyNotFoundException;
import net.dnadas.companies.exception.company.project.ProjectNotFoundException;
import net.dnadas.companies.exception.company.project.task.TaskNotFoundException;
import net.dnadas.companies.model.authorization.PermissionType;
import net.dnadas.companies.model.company.Company;
import net.dnadas.companies.model.company.CompanyDao;
import net.dnadas.companies.model.company.project.Project;
import net.dnadas.companies.model.company.project.ProjectDao;
import net.dnadas.companies.model.company.project.task.Task;
import net.dnadas.companies.model.company.project.task.TaskDao;
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
   * @param permission         A representation of the authorization object as supplied by the
   *                           expression system. Not null.
   * @return true if the authorization is granted, false otherwise
   */
  @Override
  @Transactional(readOnly = true)
  public boolean hasPermission(
    Authentication authentication, Object targetDomainObject, Object permission) {
    if ((authentication == null) || (targetDomainObject == null) ||
      !(permission instanceof PermissionType)) {
      return false;
    }
    UserInfoDto userInfo = (UserInfoDto) authentication.getPrincipal();

    if (targetDomainObject instanceof Company) {
      return handleCompanyPermissions(
        userInfo, (Company) targetDomainObject, (PermissionType) permission);
    } else if (targetDomainObject instanceof Project) {
      return handleProjectPermissions(
        userInfo, (Project) targetDomainObject, (PermissionType) permission);
    } else if (targetDomainObject instanceof Task) {
      return handleTaskPermissions(
        userInfo, (Task) targetDomainObject, (PermissionType) permission);
    }
    return false;
  }

  /**
   * Alternative method for evaluating a authorization where only the identifier of the
   * target object is available, rather than the target instance itself.
   *
   * @param authentication <strong>Passed in automatically</strong> from the {@link org.springframework.security.core.context.SecurityContextHolder}<br>
   *                       Represents the user in question. Should not be null.
   * @param targetId       The identifier for the object instance (usually a Long)
   * @param targetType     A String representing the target's type (usually a Java
   *                       classname). Not null.
   * @param permission     A representation of the authorization object as supplied by the
   *                       expression system. Not null.
   * @return true if the authorization is granted, false otherwise
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
    UserInfoDto userInfoDto = (UserInfoDto) authentication.getPrincipal();

    Long id = (Long) targetId;
    PermissionType permissionType = PermissionType.valueOf(permission.toString());

    switch (targetType) {
      case "Company" -> {
        Company company = companyDao.findById(id).orElseThrow(
          () -> new CompanyNotFoundException(id));
        return handleCompanyPermissions(userInfoDto, company, permissionType);
      }
      case "Project" -> {
        Project project = projectDao.findById(id).orElseThrow(
          () -> new ProjectNotFoundException(id));
        return handleProjectPermissions(userInfoDto, project, permissionType);
      }
      case "Task" -> {
        Task task = taskDao.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        return handleTaskPermissions(userInfoDto, task, permissionType);
      }
      default -> {
        return false;
      }
    }
  }

  @Transactional(readOnly = true)
  public boolean handleCompanyPermissions(
    UserInfoDto userInfo, Company company, PermissionType permissionType) {
    switch (permissionType) {
      case COMPANY_ADMIN:
        return hasCompanyAdminAccess(userInfo, company);
      case COMPANY_EDITOR:
        return hasCompanyEditorAccess(userInfo, company);
      case COMPANY_EMPLOYEE:
        return hasCompanyEmployeeAccess(userInfo, company);
      default:
        return false;
    }
  }

  @Transactional(readOnly = true)
  public boolean handleProjectPermissions(
    UserInfoDto userInfo, Project project, PermissionType permissionType) {
    switch (permissionType) {
      case PROJECT_ADMIN:
        return hasProjectAdminAccess(userInfo, project);
      case PROJECT_EDITOR:
        return hasProjectEditorAccess(userInfo, project);
      case PROJECT_ASSIGNED_EMPLOYEE:
        return hasProjectAssignedEmployeeAccess(userInfo, project);
      default:
        return false;
    }
  }

  @Transactional(readOnly = true)
  public boolean handleTaskPermissions(
    UserInfoDto userInfo, Task task, PermissionType permissionType) {
    switch (permissionType) {
      case TASK_ASSIGNED_EMPLOYEE:
        return hasTaskAssignedEmployeeAccess(userInfo, task);
      default:
        return false;
    }
  }

  @Transactional(readOnly = true)
  public boolean hasCompanyAdminAccess(UserInfoDto userInfo, Company company) {
    return userInfo.roles().contains(GlobalRole.ADMIN) || company.getAdmins().stream().anyMatch(
      adminId -> adminId.equals(userInfo.userId()));
  }

  @Transactional(readOnly = true)
  public boolean hasCompanyEditorAccess(UserInfoDto userInfo, Company company) {
    return userInfo.roles().contains(GlobalRole.ADMIN) || company.getEditors().stream().anyMatch(
      editorId -> editorId.equals(userInfo.userId()));
  }

  @Transactional(readOnly = true)
  public boolean hasCompanyEmployeeAccess(UserInfoDto userInfo, Company company) {
    return userInfo.roles().contains(GlobalRole.ADMIN) || company.getEmployees().stream().anyMatch(
      employeeId -> employeeId.equals(userInfo.userId()));
  }

  @Transactional(readOnly = true)
  public boolean hasProjectAdminAccess(UserInfoDto userInfo, Project project) {
    return userInfo.roles().contains(GlobalRole.ADMIN) || project.getAdmins().stream().anyMatch(
      adminId -> adminId.equals(userInfo.userId())) || hasCompanyAdminAccess(
      userInfo, project.getCompany());
  }

  @Transactional(readOnly = true)
  public boolean hasProjectEditorAccess(UserInfoDto userInfo, Project project) {
    return userInfo.roles().contains(GlobalRole.ADMIN) || project.getEditors().stream().anyMatch(
      editorId -> editorId.equals(userInfo.userId())) || hasCompanyEditorAccess(
      userInfo, project.getCompany());
  }

  @Transactional(readOnly = true)
  public boolean hasProjectAssignedEmployeeAccess(
    UserInfoDto userInfo, Project project) {
    return userInfo.roles().contains(GlobalRole.ADMIN) ||
      project.getAssignedEmployees().stream().anyMatch(
        employeeId -> employeeId.equals(userInfo.userId())) ||
      hasProjectEditorAccess(userInfo, project) || hasProjectAdminAccess(userInfo, project);
  }

  @Transactional(readOnly = true)
  public boolean hasTaskAssignedEmployeeAccess(
    UserInfoDto userInfo, Task task) {
    return userInfo.roles().contains(GlobalRole.ADMIN) ||
      task.getAssignedEmployees().stream().anyMatch(
        employeeId -> employeeId.equals(userInfo.userId())) ||
      hasProjectEditorAccess(userInfo, task.getProject()) || hasProjectAdminAccess(
      userInfo, task.getProject());
  }
}
