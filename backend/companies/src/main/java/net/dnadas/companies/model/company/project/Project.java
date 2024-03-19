package net.dnadas.companies.model.company.project;

import jakarta.persistence.*;
import lombok.*;
import net.dnadas.companies.model.company.Company;
import net.dnadas.companies.model.company.project.task.Task;
import net.dnadas.companies.model.request.ProjectJoinRequest;

import java.time.Instant;
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

  @Column(length = 500)
  private String description;
  private Instant startDate;
  private Instant deadline;

  @ManyToOne
  @JoinColumn(name = "company_id")
  private Company company;

  @OneToMany(mappedBy = "project", orphanRemoval = true, cascade = CascadeType.REMOVE)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Set<Task> tasks = new HashSet<>();

  @OneToMany(mappedBy = "project", orphanRemoval = true, cascade = CascadeType.REMOVE)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Set<ProjectJoinRequest> joinRequests = new HashSet<>();

  @ElementCollection(fetch = FetchType.EAGER)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Set<Long> admins = new HashSet<>();

  @ElementCollection(fetch = FetchType.EAGER)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Set<Long> editors = new HashSet<>();

  @ElementCollection(fetch = FetchType.EAGER)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Set<Long> assignedEmployees = new HashSet<>();

  public Project(
    String name, String description, Instant startDate, Instant deadline,
    Long projectCreatorId, Company company) {
    this.name = name;
    this.description = description;
    this.startDate = startDate;
    this.deadline = deadline;
    admins.add(projectCreatorId);
    editors.add(projectCreatorId);
    assignedEmployees.add(projectCreatorId);
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

  public Set<Long> getAdmins() {
    return Set.copyOf(admins);
  }

  public void addAdmin(Long userId) {
    this.admins.add(userId);
  }

  public void removeAdmin(Long userId) {
    this.admins.remove(userId);
  }

  public Set<Long> getEditors() {
    return Set.copyOf(editors);
  }

  public void addEditor(Long userId) {
    this.editors.add(userId);
  }

  public void removeEditor(Long userId) {
    this.editors.remove(userId);
  }

  public Set<Long> getAssignedEmployees() {
    return Set.copyOf(assignedEmployees);
  }

  public void assignEmployee(Long userId) {
    this.assignedEmployees.add(userId);
  }

  public void removeEmployee(Long userId) {
    this.assignedEmployees.remove(userId);
  }
}
