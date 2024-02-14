package com.codecool.tasx.service.auth;

import com.codecool.tasx.config.auth.SecurityConfig;
import com.codecool.tasx.exception.company.CompanyNotFoundException;
import com.codecool.tasx.exception.company.project.ProjectNotFoundException;
import com.codecool.tasx.model.auth.PermissionType;
import com.codecool.tasx.model.auth.account.UserAccount;
import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.CompanyDao;
import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.company.project.ProjectDao;
import com.codecool.tasx.model.user.ApplicationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @see SecurityConfig
 * @see org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
 * @see org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
 */
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

  private final CustomAccessControlService customAccessControlService;
  private final CompanyDao companyDao;
  private final ProjectDao projectDao;
  private final Logger logger;

  @Autowired
  public CustomPermissionEvaluator(
    CustomAccessControlService customAccessControlService, CompanyDao companyDao,
    ProjectDao projectDao) {
    this.customAccessControlService = customAccessControlService;
    this.companyDao = companyDao;
    this.projectDao = projectDao;
    logger = LoggerFactory.getLogger(this.getClass());
  }

  /**
   * @param authentication     <strong>Passed in automatically</strong> from the {@link org.springframework.security.core.context.SecurityContextHolder}<br>
   *                           Represents the user in question. Should not be null.
   * @param targetDomainObject the domain object for which permissions should be
   *                           checked. May be null in which case implementations should return false, as the null
   *                           condition can be checked explicitly in the expression.
   * @param permission         a representation of the permission object as supplied by the
   *                           expression system. Not null.
   * @return true if the permission is granted, false otherwise
   */
  @Override
  public boolean hasPermission(
    Authentication authentication, Object targetDomainObject, Object permission) {
    if ((authentication == null) || (targetDomainObject == null) ||
      !(permission instanceof PermissionType)) {
      return false;
    }
    ApplicationUser applicationUser = (ApplicationUser) authentication.getPrincipal();

    if (targetDomainObject instanceof Company) {
      return handleCompanyPermissions(
        applicationUser, targetDomainObject, (PermissionType) permission);
    } else if (targetDomainObject instanceof Project) {
      return handleProjectPermissions(
        applicationUser, targetDomainObject, (PermissionType) permission);
    }
    return false;
  }

  /**
   * Alternative method for evaluating a permission where only the identifier of the
   * target object is available, rather than the target instance itself.
   *
   * @param authentication <strong>Passed in automatically</strong> from the {@link org.springframework.security.core.context.SecurityContextHolder}<br>
   *                       Represents the user in question. Should not be null.
   * @param targetId       the identifier for the object instance (usually a Long)
   * @param targetType     a String representing the target's type (usually a Java
   *                       classname). Not null.
   * @param permission     a representation of the permission object as supplied by the
   *                       expression system. Not null.
   * @return true if the permission is granted, false otherwise
   */
  @Override
  public boolean hasPermission(
    Authentication authentication, Serializable targetId, String targetType, Object permission) {
    try {
      if ((authentication == null) || (targetId == null) || (targetType.isEmpty()) ||
        (permission == null)) {
        return false;
      }
      UserAccount account = (UserAccount) authentication.getPrincipal();
      ApplicationUser applicationUser = account.getApplicationUser();
      Long id = (Long) targetId;
      PermissionType permissionType = PermissionType.valueOf(permission.toString());

      switch (targetType) {
        case "Company" -> {
          Company company = companyDao.findById(id).orElseThrow(
            () -> new CompanyNotFoundException(id)
          );
          return handleCompanyPermissions(applicationUser, company, permissionType);
        }
        case "Project" -> {
          Project project = projectDao.findById(id).orElseThrow(
            () -> new ProjectNotFoundException(id));
          return handleProjectPermissions(applicationUser, project, permissionType);
        }
        default -> {
          return false;
        }
      }
    } catch (CompanyNotFoundException | ProjectNotFoundException e) {
      logger.error(e.getMessage());
      return false;
    }
  }

  private boolean handleCompanyPermissions(
    ApplicationUser applicationUser, Object targetDomainObject, PermissionType permissionType) {
    Company company = (Company) targetDomainObject;
    switch (permissionType) {
      case COMPANY_ADMIN:
        return customAccessControlService.hasCompanyOwnerAccess(applicationUser, company);
      case COMPANY_EMPLOYEE:
        return customAccessControlService.hasCompanyEmployeeAccess(applicationUser, company);
      default:
        return false;
    }
  }

  private boolean handleProjectPermissions(
    ApplicationUser applicationUser, Object targetDomainObject, PermissionType permissionType) {
    Project project = (Project) targetDomainObject;
    switch (permissionType) {
      case PROJECT_EDITOR:
        return customAccessControlService.hasProjectOwnerAccess(applicationUser, project);
      case PROJECT_ASSIGNED_EMPLOYEE:
        return customAccessControlService.hasAssignedToProjectAccess(applicationUser, project);
      default:
        return false;
    }
  }
}
