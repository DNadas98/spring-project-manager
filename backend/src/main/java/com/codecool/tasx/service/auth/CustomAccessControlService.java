package com.codecool.tasx.service.auth;

import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.user.ApplicationUser;
import org.springframework.stereotype.Service;

/**
 * Handles parts of the access control that are based on the business logic of the application.<br>
 * Verifies different levels of access, for example company owner access, employee level access.
 */
@Service
public class CustomAccessControlService {
  /**
   * Verifies that the {@link ApplicationUser} is the owner of the {@link Company}
   *
   * @return
   */
  public boolean hasCompanyOwnerAccess(ApplicationUser applicationUser, Company company) {
    if (!company.getCompanyOwner().equals(applicationUser)) {
      return false;
    }
    return true;
  }

  /**
   * Verifies that the {@link ApplicationUser} is an employee of the {@link Company}
   * or is the owner of the {@link Company}
   *
   * @return
   */
  public boolean hasCompanyEmployeeAccess(ApplicationUser applicationUser, Company company) {
    if (!company.getEmployees().contains(applicationUser)
      && company.getCompanyOwner().equals(applicationUser)) {
      return false;
    }
    return true;
  }

  /**
   * Verifies that the {@link ApplicationUser} is the owner of the {@link Project}
   * or the owner of the {@link Company}
   *
   * @return
   */
  public boolean hasProjectOwnerAccess(ApplicationUser applicationUser, Project project) {
    if (!project.getProjectOwner().equals(applicationUser)
      && !project.getCompany().getCompanyOwner().equals(applicationUser)) {
      return false;
    }
    return true;
  }

  /**
   * Verifies that the {@link ApplicationUser} is assigned to the {@link Project}
   * or is the owner of the {@link Project}
   * or the owner of the {@link Company}
   *
   * @return
   */
  public boolean hasAssignedToProjectAccess(ApplicationUser applicationUser, Project project) {
    if (!project.getAssignedEmployees().contains(applicationUser)
      && !project.getProjectOwner().equals(applicationUser)
      && !project.getCompany().getCompanyOwner().equals(applicationUser)) {
      return false;
    }
    return true;
  }
}
