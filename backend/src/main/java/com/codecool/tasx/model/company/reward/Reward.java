package com.codecool.tasx.model.company.reward;

import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.project.task.Task;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
public class Reward {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String description;
  private long pointCost;

  @ManyToOne
  @JoinColumn(name = "company_id")
  private Company company;

  @ManyToMany
  @JoinTable(name = "reward_tasks",
    joinColumns = @JoinColumn(name = "reward_id"),
    inverseJoinColumns = @JoinColumn(name = "task_id"))
  private List<Task> tasks;

  public Reward() {
  }

  public Reward(Long id, String name, String description, Company company, long pointCost) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.company = company;
    this.pointCost = pointCost;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public long getPointCost() {
    return pointCost;
  }

  public void setPointCost(long pointCost) {
    this.pointCost = pointCost;
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

  public Company getCompany() {
    return company;
  }

  public void setCompany(Company company) {
    this.company = company;
  }

  public List<Task> getTasks() {
    return List.copyOf(tasks);
  }

  public void setTasks(List<Task> tasks) {
    this.tasks = tasks;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, company, tasks);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    Reward reward = (Reward) object;
    return Objects.equals(id, reward.id) && Objects.equals(name, reward.name) &&
      Objects.equals(description, reward.description) && Objects.equals(
      company, reward.company) && Objects.equals(tasks, reward.tasks);
  }

  @Override
  public String toString() {
    return "Reward{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", description='" + description + '\'' +
      ", company=" + company +
      ", tasks=" + tasks +
      '}';
  }
}
