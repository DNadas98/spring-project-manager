package net.dnadas.companies.model.company.project.task;

import jakarta.persistence.*;
import lombok.*;
import net.dnadas.companies.model.company.project.Project;
import net.dnadas.companies.model.company.project.task.expense.Expense;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;

  @Column(length = 500)
  private String description;
  private Importance importance;
  private Integer difficulty;
  private Instant startDate;
  private Instant deadline;
  private TaskStatus taskStatus;

  @ManyToOne
  @JoinColumn(name = "project_id")
  @ToString.Exclude
  private Project project;

  @OneToMany(mappedBy = "task", orphanRemoval = true, cascade = CascadeType.REMOVE)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Set<Expense> expenses;

  @ElementCollection(fetch = FetchType.EAGER)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Set<Long> assignedEmployees;


  public Task(
    String name, String description, Importance importance, Integer difficulty,
    Instant startDate, Instant deadline, TaskStatus taskStatus, Project project,
    Long taskCreatorId) {
    this.name = name;
    this.description = description;
    this.importance = importance;
    this.difficulty = difficulty;
    this.startDate = startDate;
    this.deadline = deadline;
    this.taskStatus = taskStatus;
    this.project = project;
    this.assignedEmployees = new HashSet<>();
    this.assignedEmployees.add(taskCreatorId);
    this.expenses = new HashSet<>();
  }

  public Set<Expense> getExpenses() {
    return Set.copyOf(expenses);
  }

  public void addExpense(Expense expense) {
    this.expenses.add(expense);
  }

  public void removeExpense(Expense expense) {
    this.expenses.remove(expense);
  }

  public Set<Long> getAssignedEmployees() {
    return Set.copyOf(this.assignedEmployees);
  }

  public void assignEmployee(Long userId) {
    this.assignedEmployees.add(userId);
  }

  public void removeEmployee(Long userId) {
    this.assignedEmployees.remove(userId);
  }
}
