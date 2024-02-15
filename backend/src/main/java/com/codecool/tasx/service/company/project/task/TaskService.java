package com.codecool.tasx.service.company.project.task;

import com.codecool.tasx.controller.dto.company.project.task.TaskCreateRequestDto;
import com.codecool.tasx.controller.dto.company.project.task.TaskResponsePublicDto;
import com.codecool.tasx.controller.dto.company.project.task.TaskUpdateRequestDto;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.exception.company.project.ProjectNotFoundException;
import com.codecool.tasx.exception.company.project.task.TaskAlreadyFinalizedException;
import com.codecool.tasx.exception.company.project.task.TaskNotFoundException;
import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.company.project.ProjectDao;
import com.codecool.tasx.model.company.project.task.Task;
import com.codecool.tasx.model.company.project.task.TaskDao;
import com.codecool.tasx.model.company.project.task.TaskStatus;
import com.codecool.tasx.model.user.ApplicationUser;
import com.codecool.tasx.model.user.ApplicationUserDao;
import com.codecool.tasx.service.auth.UserProvider;
import com.codecool.tasx.service.converter.TaskConverter;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TaskService {
  private static final Set<TaskStatus> finishedTaskStatuses = Set.of(
    TaskStatus.DONE,
    TaskStatus.FAILED);
  private final TaskDao taskDao;
  private final ProjectDao projectDao;
  private final ApplicationUserDao applicationUserDao;
  private final TaskConverter taskConverter;
  private final UserProvider userProvider;
  private final Logger logger;

  @Autowired
  public TaskService(
    TaskDao taskDao, ProjectDao projectDao, ApplicationUserDao applicationUserDao,
    TaskConverter taskConverter,
    UserProvider userProvider) {
    this.taskDao = taskDao;
    this.projectDao = projectDao;
    this.applicationUserDao = applicationUserDao;
    this.taskConverter = taskConverter;
    this.userProvider = userProvider;
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  @Transactional
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ASSIGNED_EMPLOYEE')")
  public List<TaskResponsePublicDto> getAllTasks(Long companyId, Long projectId)
    throws ProjectNotFoundException, UnauthorizedException {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    List<Task> tasks = project.getTasks();
    return taskConverter.getTaskResponsePublicDtos(tasks);
  }

  @Transactional
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ASSIGNED_EMPLOYEE')")
  public TaskResponsePublicDto getTaskById(Long companyId, Long projectId, Long taskId)
    throws UnauthorizedException {
    Task task = taskDao.findByCompanyIdAndProjectIdAndTaskId(companyId, projectId, taskId)
      .orElseThrow(() -> new TaskNotFoundException(taskId));
    return taskConverter.getTaskResponsePublicDto(task);
  }

  @Transactional
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ASSIGNED_EMPLOYEE')")
  public List<TaskResponsePublicDto> getTasksByStatus(
    Long companyId, Long projectId, TaskStatus status)
    throws ProjectNotFoundException, UnauthorizedException {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    List<Task> tasks = taskDao.findAllByProjectAndTaskStatus(project, status);
    return taskConverter.getTaskResponsePublicDtos(tasks);
  }

  @Transactional
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ASSIGNED_EMPLOYEE')")
  public List<TaskResponsePublicDto> getFinishedTasks(Long companyId, Long projectId)
    throws ProjectNotFoundException, UnauthorizedException {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    List<Task> tasks = taskDao.findAllByProjectAndTaskStatusIn(project, finishedTaskStatuses);
    return taskConverter.getTaskResponsePublicDtos(tasks);
  }

  @Transactional
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ASSIGNED_EMPLOYEE')")
  public List<TaskResponsePublicDto> getUnfinishedTasks(Long companyId, Long projectId)
    throws ProjectNotFoundException, UnauthorizedException {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    List<Task> tasks = taskDao.findAllByProjectAndTaskStatusNotIn(project, finishedTaskStatuses);
    return taskConverter.getTaskResponsePublicDtos(tasks);
  }

  @Transactional(rollbackOn = Exception.class)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ASSIGNED_EMPLOYEE')")
  public TaskResponsePublicDto createTask(
    TaskCreateRequestDto createRequestDto, Long companyId, Long projectId)
    throws ConstraintViolationException {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    ApplicationUser applicationUser = userProvider.getAuthenticatedUser();
    Task task = new Task(createRequestDto.name(), createRequestDto.description(),
      createRequestDto.importance(), createRequestDto.difficulty(), createRequestDto.startDate(),
      createRequestDto.deadline(), createRequestDto.taskStatus(), applicationUser, project);
    task.assignEmployee(applicationUser);
    taskDao.save(task);
    return taskConverter.getTaskResponsePublicDto(task);
  }

  @Transactional(rollbackOn = Exception.class)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ASSIGNED_EMPLOYEE')")
  public TaskResponsePublicDto updateTask(
    TaskUpdateRequestDto updateRequestDto, Long companyId, Long projectId, Long taskId)
    throws ConstraintViolationException {
    Task task = taskDao.findByCompanyIdAndProjectIdAndTaskId(companyId, projectId, taskId)
      .orElseThrow(() -> new TaskNotFoundException(taskId));
    verifyEditable(task);
    updateTaskDetails(updateRequestDto, task);
    if (updateRequestDto.taskStatus().equals(TaskStatus.DONE)) {
      acquirePointsForTask(task);
    }
    Task savedTask = taskDao.save(task);
    return taskConverter.getTaskResponsePublicDto(savedTask);
  }

  private void acquirePointsForTask(Task task) throws UnauthorizedException {
    List<ApplicationUser> assignedEmployees = task.getAssignedEmployees();
    for (ApplicationUser employee : assignedEmployees) {
      employee.setScore(employee.getScore() + task.calculatePoints());
      applicationUserDao.save(employee);
    }
  }

  private void verifyEditable(Task task) {
    if (finishedTaskStatuses.contains(task.getTaskStatus())) {
      throw new TaskAlreadyFinalizedException(task.getId());
    }
  }

  private void updateTaskDetails(TaskUpdateRequestDto updateRequestDto, Task task) {
    task.setName(updateRequestDto.name());
    task.setDescription(updateRequestDto.description());
    task.setImportance(updateRequestDto.importance());
    task.setDifficulty(updateRequestDto.difficulty());
    task.setStartDate(updateRequestDto.startDate());
    task.setDeadline(updateRequestDto.deadline());
    task.setTaskStatus(updateRequestDto.taskStatus());
  }

  @Transactional(rollbackOn = Exception.class)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ASSIGNED_EMPLOYEE')")
  public void deleteTask(Long companyId, Long projectId, Long taskId) {
    Task task = taskDao.findByCompanyIdAndProjectIdAndTaskId(companyId, projectId, taskId)
      .orElseThrow(() -> new TaskNotFoundException(taskId));
    taskDao.delete(task);
  }

}
