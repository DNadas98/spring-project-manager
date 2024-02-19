package com.codecool.tasx.model.user;

import com.codecool.tasx.model.auth.account.UserAccount;
import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.company.project.task.Task;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "application_user")
public class ApplicationUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String username;

  @OneToMany(mappedBy = "applicationUser", fetch = FetchType.EAGER,
    orphanRemoval = true, cascade = CascadeType.ALL)
  private Set<UserAccount> accounts = new HashSet<>();

  @Enumerated(EnumType.STRING)
  private Set<GlobalRole> globalRoles = new HashSet<>();

  @ManyToMany(mappedBy = "admins", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<Company> adminCompanies = new HashSet<>();

  @ManyToMany(mappedBy = "editors", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<Company> editorCompanies = new HashSet<>();

  @ManyToMany(mappedBy = "employees", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<Company> employeeCompanies = new HashSet<>();

  @ManyToMany(mappedBy = "admins", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<Project> adminProjects = new HashSet<>();

  @ManyToMany(mappedBy = "editors", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<Project> editorProjects = new HashSet<>();

  @ManyToMany(mappedBy = "assignedEmployees", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<Project> assignedProjects = new HashSet<>();

  @ManyToMany(mappedBy = "assignedEmployees", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<Task> assignedTasks = new HashSet<>();


  public ApplicationUser(String username) {
    this.username = username;
    globalRoles.add(GlobalRole.USER);
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

  public void removeAccount(UserAccount account) {
    accounts.remove(account);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, globalRoles);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ApplicationUser applicationUser)) {
      return false;
    }
    return Objects.equals(id, applicationUser.id) &&
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
      '}';
  }
}

