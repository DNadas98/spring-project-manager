package com.codecool.tasx.model.company;

import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.company.reward.Reward;
import com.codecool.tasx.model.user.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Company {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String name;

  private String description;

  @ManyToOne
  @JoinColumn(name = "company_owner_id")
  private User companyOwner;

  @ManyToMany
  @JoinTable(name = "company_employees", joinColumns = @JoinColumn(name = "company_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
  private List<User> employees;

  @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Project> projects;

  @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Reward> rewards;


  public Company() {
  }

  public Company(String name, String description, User companyOwner) {
    this.name = name;
    this.description = description;
    this.companyOwner = companyOwner;
    this.employees = new ArrayList<>();
    this.projects = new ArrayList<>();
    this.rewards = new ArrayList<>();
  }

  public Long getId() {
    return id;
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

  public User getCompanyOwner() {
    return companyOwner;
  }

  public List<User> getEmployees() {
    return List.copyOf(employees);
  }

  public void addEmployee(User user) {
    employees.add(user);
  }

  public List<Project> getProjects() {
    return List.copyOf(projects);
  }

  public List<Reward> getRewards() {
    return List.copyOf(rewards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, companyOwner, employees, projects, rewards);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    Company company = (Company) object;
    return Objects.equals(id, company.id) && Objects.equals(name, company.name) && Objects.equals(
      description, company.description) && Objects.equals(companyOwner, company.companyOwner) &&
      Objects.equals(employees, company.employees) && Objects.equals(projects, company.projects) &&
      Objects.equals(rewards, company.rewards);
  }

  @Override
  public String toString() {
    return "Company{" + "id=" + id + ", name='" + name + '\'' + ", description='" + description +
      '\'' + ", companyOwner=" + companyOwner + ", employees=" + employees + ", projects=" +
      projects + ", rewards=" + rewards + '}';
  }
}