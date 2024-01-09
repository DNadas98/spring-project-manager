package com.codecool.tasx.model.company.project.task.expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpenseDao extends JpaRepository<Expense, Long> {
  @Query(
    "SELECT e FROM Expense e WHERE e.task.project.company.id = :companyId" +
      " AND e.task.project.id = :projectId" +
      " AND e.task.id = :taskId" +
      " AND e.id = :expenseId")
  Optional<Expense> findByCompanyIdAndProjectIdAndTaskIdAndExpenseId(
    @Param("companyId") Long companyId,
    @Param("projectId") Long projectId,
    @Param("taskId") Long taskId,
    @Param("expenseId") Long expenseId);
}
