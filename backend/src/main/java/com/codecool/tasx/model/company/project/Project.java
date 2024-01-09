package com.codecool.tasx.model.company.project;

import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.project.task.Task;
import com.codecool.tasx.model.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Project {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String description;
  private LocalDateTime startDate;
  private LocalDateTime deadline;

  @ManyToOne
  @JoinColumn(name = "project_owner_id")
  private User projectOwner;

  @ManyToMany
  @JoinTable(name = "project_assigned_employees", joinColumns = @JoinColumn(name = "project_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
  private List<User> assignedEmployees;

  @ManyToOne
  @JoinColumn(name = "company_id")
  private Company company;

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Task> tasks;

  public Project() {
  }


  public Project(
    String name, String description, LocalDateTime startDate, LocalDateTime deadline,
    User projectOwner, Company company) {
    this.name = name;
    this.description = description;
    this.startDate = startDate;
    this.deadline = deadline;
    this.projectOwner = projectOwner;
    this.assignedEmployees = new ArrayList<>();
    this.company = company;
    this.tasks = new ArrayList<>();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDateTime startDate) {
    this.startDate = startDate;
  }

  public LocalDateTime getDeadline() {
    return deadline;
  }

  public void setDeadline(LocalDateTime deadline) {
    this.deadline = deadline;
  }

  public User getProjectOwner() {
    return projectOwner;
  }

  public void setProjectOwner(User projectOwner) {
    this.projectOwner = projectOwner;
  }

  public List<User> getAssignedEmployees() {
    return List.copyOf(assignedEmployees);
  }

  public Company getCompany() {
    return company;
  }

  public void setCompany(Company company) {
    this.company = company;
  }

  public List<Task> getTasks() {
    return List.copyOf(tasks);
  }

  public void assignEmployee(User employee) {
    assignedEmployees.add(employee);
  }

  public void removeEmployee(User employee) {
    assignedEmployees.remove(employee);
  }

  public void addTask(Task task) {
    tasks.add(task);
  }

  public void removeTask(Task task) {
    tasks.remove(task);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, startDate, deadline, projectOwner, assignedEmployees,
      company, tasks);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    Project project = (Project) object;
    return Objects.equals(id, project.id) && Objects.equals(name, project.name) &&
      Objects.equals(description, project.description) && Objects.equals(
      startDate, project.startDate) && Objects.equals(deadline, project.deadline) &&
      Objects.equals(projectOwner, project.projectOwner) && Objects.equals(
      assignedEmployees, project.assignedEmployees) && Objects.equals(
      company, project.company) && Objects.equals(tasks, project.tasks);
  }

  @Override
  public String toString() {
    return "Project{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", description='" + description + '\'' +
      ", startDate=" + startDate +
      ", deadline=" + deadline +
      ", projectOwner=" + projectOwner +
      ", assignedEmployees=" + assignedEmployees +
      ", company=" + company +
      ", tasks=" + tasks +
      '}';
  }
}
