package com.codecool.tasx.service.company.project.task.expense;

import com.codecool.tasx.controller.dto.company.project.task.expense.ExpenseCreateRequestDto;
import com.codecool.tasx.controller.dto.company.project.task.expense.ExpenseResponseDto;
import com.codecool.tasx.controller.dto.company.project.task.expense.ExpenseUpdateRequestDto;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.exception.company.project.ProjectNotFoundException;
import com.codecool.tasx.exception.company.project.task.TaskNotFoundException;
import com.codecool.tasx.exception.company.project.task.expense.ExpenseNotFoundException;
import com.codecool.tasx.model.company.project.task.Task;
import com.codecool.tasx.model.company.project.task.TaskDao;
import com.codecool.tasx.model.company.project.task.expense.Expense;
import com.codecool.tasx.model.company.project.task.expense.ExpenseDao;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
  private final TaskDao taskDao;
  private final ExpenseDao expenseDao;
  private final Logger logger;

  @Autowired
  public ExpenseService(
    TaskDao taskDao, ExpenseDao expenseDao) {
    this.taskDao = taskDao;
    this.expenseDao = expenseDao;
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  @Transactional
  @PreAuthorize("hasPermission(#projectId, 'Project', Role.PROJECT_ASSINGED_EMPLOYEE)")
  public List<ExpenseResponseDto> getAllExpenses(Long companyId, Long projectId, Long taskId)
    throws ProjectNotFoundException, UnauthorizedException {
    Task task = taskDao.findByCompanyIdAndProjectIdAndTaskId(companyId, projectId, taskId)
      .orElseThrow(() -> new TaskNotFoundException(taskId));
    List<Expense> expenses = task.getExpenses();
    return expenses.stream().map(
      expense -> new ExpenseResponseDto(expense.getId(), expense.getName(), expense.getPrice(),
        expense.isPaid())).collect(Collectors.toList());
  }

  @Transactional
  @PreAuthorize("hasPermission(#projectId, 'Project', Role.PROJECT_ASSINGED_EMPLOYEE)")
  public ExpenseResponseDto getExpense(
    Long companyId, Long projectId, Long taskId, Long expenseId) throws UnauthorizedException {
    Expense expense = expenseDao.findByCompanyIdAndProjectIdAndTaskIdAndExpenseId(
      companyId, projectId, taskId, expenseId).orElseThrow(() -> new ExpenseNotFoundException());
    return new ExpenseResponseDto(expense.getId(), expense.getName(), expense.getPrice(),
      expense.isPaid());
  }

  @Transactional(rollbackOn = Exception.class)
  @PreAuthorize("hasPermission(#projectId, 'Project', Role.PROJECT_ASSINGED_EMPLOYEE)")
  public ExpenseResponseDto createExpense(
    ExpenseCreateRequestDto createRequestDto, Long companyId, Long projectId, Long taskId)
    throws ConstraintViolationException {
    Task task = taskDao.findByCompanyIdAndProjectIdAndTaskId(companyId, projectId, taskId)
      .orElseThrow(() -> new TaskNotFoundException(taskId));
    Expense expense = new Expense(createRequestDto.name(), createRequestDto.price(),
      createRequestDto.paid(), task);
    Expense savedExpense = expenseDao.save(expense);
    return new ExpenseResponseDto(savedExpense.getId(), savedExpense.getName(),
      savedExpense.getPrice(), savedExpense.isPaid());
  }

  @Transactional(rollbackOn = Exception.class)
  @PreAuthorize("hasPermission(#projectId, 'Project', Role.PROJECT_ASSINGED_EMPLOYEE)")
  public ExpenseResponseDto updateExpense(
    ExpenseUpdateRequestDto updateRequestDto, Long companyId, Long projectId, Long taskId,
    Long expenseId) throws ConstraintViolationException {
    Expense expense = expenseDao.findByCompanyIdAndProjectIdAndTaskIdAndExpenseId(
      companyId, projectId, taskId, expenseId).orElseThrow(() -> new ExpenseNotFoundException());
    expense.setName(updateRequestDto.name());
    expense.setPrice(updateRequestDto.price());
    expense.setPaid(updateRequestDto.paid());
    Expense savedExpense = expenseDao.save(expense);
    return new ExpenseResponseDto(savedExpense.getId(), savedExpense.getName(),
      savedExpense.getPrice(), savedExpense.isPaid());
  }

  @Transactional(rollbackOn = Exception.class)
  @PreAuthorize("hasPermission(#projectId, 'Project', Role.PROJECT_ASSINGED_EMPLOYEE)")
  public void deleteExpense(Long companyId, Long projectId, Long taskId, Long expenseId) {
    Expense expense = expenseDao.findByCompanyIdAndProjectIdAndTaskIdAndExpenseId(
      companyId, projectId, taskId, expenseId).orElseThrow(() -> new ExpenseNotFoundException());
    expenseDao.delete(expense);
  }

}
