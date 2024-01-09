package com.codecool.tasx.service.auth;

import com.codecool.tasx.exception.auth.UnauthorizedException;
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
   */
  public void verifyCompanyOwnerAccess(Company company, User user) throws UnauthorizedException {
    if (!company.getCompanyOwner().equals(user)) {
      throw new UnauthorizedException(
        "User with ID " + user.getId() + " is not the owner of company with ID " + company.getId());
    }
  }

  /**
   * Verifies that the {@link User} is an employee of the {@link Company}
   * or is the owner of the {@link Company}
   */
  public void verifyCompanyEmployeeAccess(Company company, User user) throws UnauthorizedException {
    if (!company.getEmployees().contains(user)
      && company.getCompanyOwner().equals(user)) {
      throw new UnauthorizedException(
        "User with ID " + user.getId() + " is not employed by company with ID " + company.getId());
    }
  }

  /**
   * Verifies that the {@link User} is the owner of the {@link Project}
   * or the owner of the {@link Company}
   */
  public void verifyProjectOwnerAccess(Project project, User user) throws UnauthorizedException {
    if (!project.getProjectOwner().equals(user)
      && !project.getCompany().getCompanyOwner().equals(user)) {
      throw new UnauthorizedException(
        "User with ID " + user.getId() + " is not the owner of project with ID " + project.getId());
    }
  }

  /**
   * Verifies that the {@link User} is assigned to the {@link Project}
   * or is the owner of the {@link Project}
   * or the owner of the {@link Company}
   */
  public void verifyAssignedToProjectAccess(Project project, User user)
    throws UnauthorizedException {
    if (!project.getAssignedEmployees().contains(user)
      && !project.getProjectOwner().equals(user)
      && !project.getCompany().getCompanyOwner().equals(user)) {
      throw new UnauthorizedException(
        "User with ID " + user.getId() + " is not assigned to project with ID " + project.getId());
    }
  }
}
