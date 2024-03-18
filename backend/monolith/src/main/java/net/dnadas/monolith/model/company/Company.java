package net.dnadas.monolith.model.company;

import jakarta.persistence.*;
import lombok.*;
import net.dnadas.monolith.model.company.project.Project;
import net.dnadas.monolith.model.request.CompanyJoinRequest;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Company {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String name;

  @Column(length = 500)
  private String description;

  @OneToMany(mappedBy = "company", orphanRemoval = true, cascade = CascadeType.REMOVE)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Set<Project> projects;

  @OneToMany(mappedBy = "company", orphanRemoval = true, cascade = CascadeType.REMOVE)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Set<CompanyJoinRequest> joinRequests = new HashSet<>();

  @ElementCollection(fetch = FetchType.LAZY)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Set<Long> admins;

  @ElementCollection(fetch = FetchType.LAZY)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Set<Long> editors;

  @ElementCollection(fetch = FetchType.LAZY)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Set<Long> employees;

  public Company(String name, String description, Long companyCreatorId) {
    this.name = name;
    this.description = description;
    this.admins = new HashSet<>();
    this.editors = new HashSet<>();
    this.employees = new HashSet<>();
    this.admins.add(companyCreatorId);
    this.editors.add(companyCreatorId);
    this.employees.add(companyCreatorId);
    this.projects = new HashSet<>();
  }

  public Set<Project> getProjects() {
    return Set.copyOf(projects);
  }

  public Set<Long> getAdmins() {
    return Set.copyOf(admins);
  }

  public void addAdmin(Long userId) {
    admins.add(userId);
  }

  public void removeAdmin(Long userId) {
    admins.remove(userId);
  }

  public Set<Long> getEditors() {
    return Set.copyOf(editors);
  }

  public void addEditor(Long userId) {
    editors.add(userId);
  }

  public void removeEditor(Long userId) {
    editors.remove(userId);
  }

  public Set<Long> getEmployees() {
    return Set.copyOf(employees);
  }

  public void addEmployee(Long userId) {
    employees.add(userId);
  }

  public void removeEmployee(Long userId) {
    employees.remove(userId);
  }
}