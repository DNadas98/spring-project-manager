package com.codecool.tasx.model.company.project;

import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.project.task.Task;
import com.codecool.tasx.model.user.ApplicationUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Project {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String description;
  private LocalDateTime startDate;
  private LocalDateTime deadline;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "company_id")
  private Company company;

  @OneToMany(mappedBy = "project", orphanRemoval = true)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Set<Task> tasks = new HashSet<>();

  @ManyToMany
  @JoinTable(name = "project_admins", joinColumns = @JoinColumn(name = "project_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Set<ApplicationUser> admins = new HashSet<>();

  @ManyToMany
  @JoinTable(name = "project_editors", joinColumns = @JoinColumn(name = "project_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Set<ApplicationUser> editors = new HashSet<>();

  @ManyToMany
  @JoinTable(name = "project_assigned_employees", joinColumns = @JoinColumn(name = "project_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Set<ApplicationUser> assignedEmployees = new HashSet<>();

  public Project(
    String name, String description, LocalDateTime startDate, LocalDateTime deadline,
    ApplicationUser projectCreator, Company company) {
    this.name = name;
    this.description = description;
    this.startDate = startDate;
    this.deadline = deadline;
    admins.add(projectCreator);
    editors.add(projectCreator);
    assignedEmployees.add(projectCreator);
    this.company = company;
  }

  public Set<Task> getTasks() {
    return Set.copyOf(tasks);
  }

  public void addTask(Task task) {
    this.tasks.add(task);
  }

  public void removeTask(Task task) {
    this.tasks.remove(task);
  }

  public Set<ApplicationUser> getAdmins() {
    return Set.copyOf(admins);
  }

  public void addAdmin(ApplicationUser applicationUser) {
    this.admins.add(applicationUser);
  }

  public void removeAdmin(ApplicationUser applicationUser) {
    this.admins.remove(applicationUser);
  }

  public Set<ApplicationUser> getEditors() {
    return Set.copyOf(editors);
  }

  public void addEditor(ApplicationUser applicationUser) {
    this.editors.add(applicationUser);
  }

  public void removeEditor(ApplicationUser applicationUser) {
    this.editors.remove(applicationUser);
  }

  public Set<ApplicationUser> getAssignedEmployees() {
    return Set.copyOf(assignedEmployees);
  }

  public void assignEmployee(ApplicationUser applicationUser) {
    this.assignedEmployees.add(applicationUser);
  }

  public void removeEmployee(ApplicationUser applicationUser) {
    this.assignedEmployees.remove(applicationUser);
  }
}
