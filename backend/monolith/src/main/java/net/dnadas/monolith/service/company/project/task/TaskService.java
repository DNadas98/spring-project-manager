package net.dnadas.monolith.service.company.project.task;

import lombok.RequiredArgsConstructor;
import net.dnadas.auth.exception.authentication.UnauthorizedException;
import net.dnadas.monolith.dto.company.project.task.TaskCreateRequestDto;
import net.dnadas.monolith.dto.company.project.task.TaskResponsePublicDto;
import net.dnadas.monolith.dto.company.project.task.TaskUpdateRequestDto;
import net.dnadas.monolith.exception.company.project.ProjectNotFoundException;
import net.dnadas.monolith.exception.company.project.task.TaskNotFoundException;
import net.dnadas.monolith.model.company.project.Project;
import net.dnadas.monolith.model.company.project.ProjectDao;
import net.dnadas.monolith.model.company.project.task.Task;
import net.dnadas.monolith.model.company.project.task.TaskDao;
import net.dnadas.monolith.model.company.project.task.TaskStatus;
import net.dnadas.monolith.service.converter.TaskConverter;
import net.dnadas.monolith.service.datetime.DateTimeService;
import net.dnadas.monolith.service.user.UserProvider;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
  private final TaskDao taskDao;
  private final ProjectDao projectDao;
  private final TaskConverter taskConverter;
  private final UserProvider userProvider;
  private final DateTimeService dateTimeService;

  @Transactional(readOnly = true)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ASSIGNED_EMPLOYEE')")
  public List<TaskResponsePublicDto> getAllTasks(Long companyId, Long projectId)
    throws ProjectNotFoundException, UnauthorizedException {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));

    List<Task> tasks = project.getTasks().stream().toList();
    return taskConverter.getTaskResponsePublicDtos(tasks);
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ASSIGNED_EMPLOYEE')")
  public List<TaskResponsePublicDto> getAllTasks(Long companyId, Long projectId, Boolean withUser)
    throws ProjectNotFoundException, UnauthorizedException {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    Long userId = userProvider.getAuthenticatedUserId();
    List<Task> tasks;
    if (withUser) {
      tasks = taskDao.findAllByProjectAndApplicationUser(project, userId);
    } else {
      tasks = taskDao.findAllByProjectAndWithoutApplicationUser(project, userId);
    }
    return taskConverter.getTaskResponsePublicDtos(tasks);
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ASSIGNED_EMPLOYEE')")
  public List<TaskResponsePublicDto> getAllTasks(
    Long companyId, Long projectId, Boolean withUser, TaskStatus taskStatus)
    throws ProjectNotFoundException, UnauthorizedException {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    Long userId = userProvider.getAuthenticatedUserId();
    List<Task> tasks;
    if (withUser) {
      tasks = taskDao.findAllByProjectAndTaskStatusAndApplicationUser(project, taskStatus, userId);
    } else {
      tasks = taskDao.findAllByProjectAndTaskStatusAndWithoutApplicationUser(project, taskStatus,
        userId);
    }
    return taskConverter.getTaskResponsePublicDtos(tasks);
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#projectId, 'Project', 'PROJECT_ASSIGNED_EMPLOYEE')")
  public TaskResponsePublicDto createTask(
    TaskCreateRequestDto createRequestDto, Long companyId, Long projectId)
    throws ConstraintViolationException {
    Instant taskStartDate = dateTimeService.toStoredDate(createRequestDto.startDate());
    Instant taskDeadline = dateTimeService.toStoredDate(createRequestDto.deadline());

    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));

    Long userId = userProvider.getAuthenticatedUserId();
    dateTimeService.validateTaskDates(taskStartDate, taskDeadline, project.getStartDate(),
      project.getDeadline());

    Task task = new Task(createRequestDto.name(), createRequestDto.description(),
      createRequestDto.importance(), createRequestDto.difficulty(), taskStartDate, taskDeadline,
      createRequestDto.taskStatus(), project, userId);
    taskDao.save(task);
    return taskConverter.getTaskResponsePublicDto(task);
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasPermission(#taskId, 'Task', 'TASK_ASSIGNED_EMPLOYEE')")
  public TaskResponsePublicDto getTaskById(Long companyId, Long projectId, Long taskId)
    throws UnauthorizedException {
    Task task = taskDao.findByCompanyIdAndProjectIdAndTaskId(companyId, projectId, taskId)
      .orElseThrow(() -> new TaskNotFoundException(taskId));
    return taskConverter.getTaskResponsePublicDto(task);
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#taskId, 'Task', 'TASK_ASSIGNED_EMPLOYEE')")
  public TaskResponsePublicDto updateTask(
    TaskUpdateRequestDto updateRequestDto, Long companyId, Long projectId, Long taskId)
    throws ConstraintViolationException {
    Instant taskStartDate = dateTimeService.toStoredDate(updateRequestDto.startDate());
    Instant taskDeadline = dateTimeService.toStoredDate(updateRequestDto.deadline());
    Task task = taskDao.findByCompanyIdAndProjectIdAndTaskId(companyId, projectId, taskId)
      .orElseThrow(() -> new TaskNotFoundException(taskId));
    Project project = task.getProject();
    dateTimeService.validateTaskDates(taskStartDate, taskDeadline, project.getStartDate(),
      project.getDeadline());

    updateTaskDetails(updateRequestDto, task, taskStartDate, taskDeadline);
    Task savedTask = taskDao.save(task);
    return taskConverter.getTaskResponsePublicDto(savedTask);
  }

  private void updateTaskDetails(
    TaskUpdateRequestDto updateRequestDto, Task task, Instant taskStartDate, Instant taskDeadline) {
    task.setName(updateRequestDto.name());
    task.setDescription(updateRequestDto.description());
    task.setImportance(updateRequestDto.importance());
    task.setDifficulty(updateRequestDto.difficulty());
    task.setStartDate(taskStartDate);
    task.setDeadline(taskDeadline);
    task.setTaskStatus(updateRequestDto.taskStatus());
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#taskId, 'Task', 'TASK_ASSIGNED_EMPLOYEE')")
  public void deleteTask(Long companyId, Long projectId, Long taskId) {
    Task task = taskDao.findByCompanyIdAndProjectIdAndTaskId(companyId, projectId, taskId)
      .orElseThrow(() -> new TaskNotFoundException(taskId));
    taskDao.delete(task);
  }
}
