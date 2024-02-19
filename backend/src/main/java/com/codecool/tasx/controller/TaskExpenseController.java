package com.codecool.tasx.controller;

import com.codecool.tasx.dto.company.project.task.expense.ExpenseCreateRequestDto;
import com.codecool.tasx.dto.company.project.task.expense.ExpenseResponseDto;
import com.codecool.tasx.dto.company.project.task.expense.ExpenseUpdateRequestDto;
import com.codecool.tasx.service.company.project.task.expense.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/companies/{companyId}/projects/{projectId}/tasks/{taskId}/expenses")
@RequiredArgsConstructor
public class TaskExpenseController {
  private final ExpenseService expenseService;

  @GetMapping
  public ResponseEntity<?> getAllExpensesOfTask(
    @PathVariable Long companyId, @PathVariable Long projectId, @PathVariable Long taskId) {
    List<ExpenseResponseDto> expenses = expenseService.getAllExpenses(companyId, projectId, taskId);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", expenses));
  }

  @GetMapping("/sum")
  public ResponseEntity<?> sumExpensesOfTask(
    @PathVariable Long companyId, @PathVariable Long projectId, @PathVariable Long taskId,
    @RequestParam(name = "paid", required = false) Boolean paid) {
    Double sum;
    if (paid == null) {
      sum = expenseService.sumAllExpensesInTask(companyId, projectId, taskId);
    } else if (paid) {
      sum = expenseService.sumPaidExpensesInTask(companyId, projectId, taskId);
    } else {
      sum = expenseService.sumUnpaidExpensesInTask(companyId, projectId, taskId);
    }
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", sum));
  }

  @GetMapping("/{expenseId}")
  public ResponseEntity<?> getExpenseById(
    @PathVariable Long companyId, @PathVariable Long projectId, @PathVariable Long taskId,
    @PathVariable Long expenseId) {
    ExpenseResponseDto expense = expenseService.getExpense(companyId, projectId, taskId, expenseId);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", expense));
  }

  @PostMapping
  public ResponseEntity<?> createExpense(
    @PathVariable Long companyId, @PathVariable Long projectId, @PathVariable Long taskId,
    @RequestBody ExpenseCreateRequestDto createRequestDto) {
    ExpenseResponseDto expense = expenseService.createExpense(createRequestDto, companyId,
      projectId, taskId);
    return ResponseEntity.status(HttpStatus.CREATED).body(
      Map.of("message", "Expense created successfully", "data", expense));
  }

  @PutMapping("/{expenseId}")
  public ResponseEntity<?> updateExpense(
    @PathVariable Long companyId, @PathVariable Long projectId, @PathVariable Long taskId,
    @PathVariable Long expenseId, @RequestBody ExpenseUpdateRequestDto updateRequestDto) {
    ExpenseResponseDto expense = expenseService.updateExpense(updateRequestDto, companyId,
      projectId, taskId, expenseId);

    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Expense with ID " + expenseId + " updated successfully", "data", expense));
  }

  @DeleteMapping("/{expenseId}")
  public ResponseEntity<?> deleteExpense(
    @PathVariable Long companyId, @PathVariable Long projectId, @PathVariable Long taskId,
    @PathVariable Long expenseId) {
    expenseService.deleteExpense(companyId, projectId, taskId, expenseId);

    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Expense with ID " + expenseId + " deleted successfully"));
  }
}
