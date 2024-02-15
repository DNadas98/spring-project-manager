package com.codecool.tasx.model.user;

import com.codecool.tasx.model.auth.account.UserAccount;
import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.project.Project;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@Table(name = "application_user")
public class ApplicationUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToMany(mappedBy = "applicationUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<UserAccount> accounts;

  @Column(nullable = false)
  private String username;

  @Enumerated(EnumType.STRING)
  private Set<GlobalRole> globalRoles;

  @OneToMany(mappedBy = "companyOwner", fetch = FetchType.EAGER)
  private Set<Company> ownedCompanies;

  @ManyToMany(mappedBy = "employees", fetch = FetchType.EAGER)
  private Set<Company> companies;

  @OneToMany(mappedBy = "projectOwner", fetch = FetchType.EAGER)
  private Set<Project> ownedProjects;

  @ManyToMany(mappedBy = "assignedEmployees", fetch = FetchType.EAGER)
  private Set<Project> projects;

  @Column(nullable = false)
  private long score;

  public ApplicationUser(String username) {
    this.username = username;
    this.ownedCompanies = new HashSet<>();
    this.companies = new HashSet<>();
    this.ownedProjects = new HashSet<>();
    this.projects = new HashSet<>();
    this.score = 0;
    this.globalRoles = new HashSet<>();
    this.accounts = new HashSet<>();
    globalRoles.add(GlobalRole.USER);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Set<GlobalRole> getGlobalRoles() {
    return Set.copyOf(globalRoles);
  }

  public void addGlobalRole(GlobalRole globalRole) {
    this.globalRoles.add(globalRole);
  }

  public void removeGlobalRole(GlobalRole globalRole) {
    this.globalRoles.remove(globalRole);
  }

  public Set<UserAccount> getAccounts() {
    return Set.copyOf(accounts);
  }

  public long getScore() {
    return score;
  }

  public void setScore(long score) {
    this.score = score;
  }

  public Set<Company> getOwnedCompanies() {
    return Set.copyOf(ownedCompanies);
  }

  public void addOwnedCompany(Company company) {
    this.ownedCompanies.add(company);
  }

  public void removeOwnedCompany(Company company) {
    this.ownedCompanies.remove(company);
  }

  public Set<Project> getProjects() {
    return projects;
  }

  public void addProject(Project project) {
    this.projects.add(project);
  }

  public void removeProject(Project project) {
    this.projects.remove(project);
  }

  public List<Company> getCompanies() {
    return List.copyOf(companies);
  }

  public void addCompany(Company company) {
    this.companies.add(company);
  }

  public void removeCompany(Company company) {
    this.companies.remove(company);
  }

  public List<Project> getOwnedProjects() {
    return List.copyOf(ownedProjects);
  }

  public void addOwnedProject(Project ownedProject) {
    this.ownedProjects.add(ownedProject);
  }

  public void removeOwnedProject(Project ownedProject) {
    this.ownedProjects.remove(ownedProject);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, globalRoles, score);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ApplicationUser applicationUser)) {
      return false;
    }
    return score == applicationUser.score && Objects.equals(id, applicationUser.id) &&
      Objects.equals(
        username, applicationUser.username) && Objects.equals(
      globalRoles, applicationUser.globalRoles);
  }

  @Override
  public String toString() {
    return "ApplicationUser{" +
      "id=" + id +
      ", username='" + username + '\'' +
      ", globalRoles=" + globalRoles +
      ", score=" + score +
      '}';
  }
}

