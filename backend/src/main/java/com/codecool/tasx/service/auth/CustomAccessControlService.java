package com.codecool.tasx.service.auth;

import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.user.User;
import org.springframework.stereotype.Service;

/**
 * Handles parts of the access control that are based on the business logic of the application.<br>
 * Verifies different levels of access, for example company owner access, employee level access.
 */
@Service
public class CustomAccessControlService {
  /**
   * Verifies that the {@link User} is the owner of the {@link Company}
   *
   * @return
   */
  public boolean hasCompanyOwnerAccess(User user, Company company) {
    if (!company.getCompanyOwner().equals(user)) {
      return false;
    }
    return true;
  }

  /**
   * Verifies that the {@link User} is an employee of the {@link Company}
   * or is the owner of the {@link Company}
   *
   * @return
   */
  public boolean hasCompanyEmployeeAccess(User user, Company company) {
    if (!company.getEmployees().contains(user)
      && company.getCompanyOwner().equals(user)) {
      return false;
    }
    return true;
  }

  /**
   * Verifies that the {@link User} is the owner of the {@link Project}
   * or the owner of the {@link Company}
   *
   * @return
   */
  public boolean hasProjectOwnerAccess(User user, Project project) {
    if (!project.getProjectOwner().equals(user)
      && !project.getCompany().getCompanyOwner().equals(user)) {
      return false;
    }
    return true;
  }

  /**
   * Verifies that the {@link User} is assigned to the {@link Project}
   * or is the owner of the {@link Project}
   * or the owner of the {@link Company}
   *
   * @return
   */
  public boolean hasAssignedToProjectAccess(User user, Project project) {
    if (!project.getAssignedEmployees().contains(user)
      && !project.getProjectOwner().equals(user)
      && !project.getCompany().getCompanyOwner().equals(user)) {
      return false;
    }
    return true;
  }
}
