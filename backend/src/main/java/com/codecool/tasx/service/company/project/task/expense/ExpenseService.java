package com.codecool.tasx.service.company.project.task.expense;

import com.codecool.tasx.controller.dto.company.project.task.expense.ExpenseCreateRequestDto;
import com.codecool.tasx.controller.dto.company.project.task.expense.ExpenseResponseDto;
import com.codecool.tasx.controller.dto.company.project.task.expense.ExpenseUpdateRequestDto;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.exception.company.project.ProjectNotFoundException;
import com.codecool.tasx.exception.company.project.task.TaskNotFoundException;
import com.codecool.tasx.exception.company.project.task.expense.ExpenseNotFoundException;
import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.company.project.ProjectDao;
import com.codecool.tasx.model.company.project.task.Task;
import com.codecool.tasx.model.company.project.task.TaskDao;
import com.codecool.tasx.model.company.project.task.expense.Expense;
import com.codecool.tasx.model.company.project.task.expense.ExpenseDao;
import com.codecool.tasx.model.user.User;
import com.codecool.tasx.service.auth.CustomAccessControlService;
import com.codecool.tasx.service.auth.UserProvider;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
  private final TaskDao taskDao;
  private final ExpenseDao expenseDao;
  private final ProjectDao projectDao;
  private final UserProvider userProvider;
  private final CustomAccessControlService accessControlService;
  private final Logger logger;

  @Autowired
  public ExpenseService(
    TaskDao taskDao, ExpenseDao expenseDao, ProjectDao projectDao, UserProvider userProvider,
    CustomAccessControlService accessControlService) {
    this.taskDao = taskDao;
    this.expenseDao = expenseDao;
    this.projectDao = projectDao;
    this.userProvider = userProvider;
    this.accessControlService = accessControlService;
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  @Transactional
  public List<ExpenseResponseDto> getAllExpenses(Long companyId, Long projectId, Long taskId)
    throws ProjectNotFoundException, UnauthorizedException {
    Task task = taskDao.findByCompanyIdAndProjectIdAndTaskId(companyId, projectId, taskId)
      .orElseThrow(() -> new TaskNotFoundException(taskId));
    User user = userProvider.getAuthenticatedUser();
    Company company = task.getProject().getCompany();
    accessControlService.verifyCompanyEmployeeAccess(company, user);
    List<Expense> expenses = task.getExpenses();
    return expenses.stream().map(
      expense -> new ExpenseResponseDto(expense.getId(), expense.getName(), expense.getPrice(),
        expense.isPaid())).collect(Collectors.toList());
  }

  @Transactional
  public ExpenseResponseDto getExpense(
    Long companyId, Long projectId, Long taskId, Long expenseId) throws UnauthorizedException {
    Expense expense = expenseDao.findByCompanyIdAndProjectIdAndTaskIdAndExpenseId(
      companyId, projectId, taskId, expenseId).orElseThrow(() -> new ExpenseNotFoundException());
    User user = userProvider.getAuthenticatedUser();
    Project project = expense.getTask().getProject();
    accessControlService.verifyAssignedToProjectAccess(project, user);
    return new ExpenseResponseDto(expense.getId(), expense.getName(), expense.getPrice(),
      expense.isPaid());
  }

  @Transactional(rollbackOn = Exception.class)
  public ExpenseResponseDto createExpense(
    ExpenseCreateRequestDto createRequestDto, Long companyId, Long projectId, Long taskId)
    throws ConstraintViolationException {
    Task task = taskDao.findByCompanyIdAndProjectIdAndTaskId(companyId, projectId, taskId)
      .orElseThrow(() -> new TaskNotFoundException(taskId));
    User user = userProvider.getAuthenticatedUser();
    Project project = task.getProject();
    accessControlService.verifyAssignedToProjectAccess(project, user);
    Expense expense = new Expense(createRequestDto.name(), createRequestDto.price(),
      createRequestDto.paid(), task);
    Expense savedExpense = expenseDao.save(expense);
    return new ExpenseResponseDto(savedExpense.getId(), savedExpense.getName(),
      savedExpense.getPrice(), savedExpense.isPaid());
  }

  @Transactional(rollbackOn = Exception.class)
  public ExpenseResponseDto updateExpense(
    ExpenseUpdateRequestDto updateRequestDto, Long companyId, Long projectId, Long taskId,
    Long expenseId) throws ConstraintViolationException {
    User user = userProvider.getAuthenticatedUser();
    Expense expense = expenseDao.findByCompanyIdAndProjectIdAndTaskIdAndExpenseId(
      companyId, projectId, taskId, expenseId).orElseThrow(() -> new ExpenseNotFoundException());
    Project project = expense.getTask().getProject();
    accessControlService.verifyAssignedToProjectAccess(project, user);
    expense.setName(updateRequestDto.name());
    expense.setPrice(updateRequestDto.price());
    expense.setPaid(updateRequestDto.paid());
    Expense savedExpense = expenseDao.save(expense);
    return new ExpenseResponseDto(savedExpense.getId(), savedExpense.getName(),
      savedExpense.getPrice(), savedExpense.isPaid());
  }

  @Transactional(rollbackOn = Exception.class)
  public void deleteExpense(Long companyId, Long projectId, Long taskId, Long expenseId) {
    User user = userProvider.getAuthenticatedUser();
    Expense expense = expenseDao.findByCompanyIdAndProjectIdAndTaskIdAndExpenseId(
      companyId, projectId, taskId, expenseId).orElseThrow(() -> new ExpenseNotFoundException());
    Project project = expense.getTask().getProject();
    accessControlService.verifyAssignedToProjectAccess(project, user);
    expenseDao.delete(expense);
  }

}
