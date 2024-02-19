package com.codecool.tasx.service.company.project.task;

import com.codecool.tasx.dto.user.UserResponsePublicDto;
import com.codecool.tasx.exception.company.project.task.TaskNotFoundException;
import com.codecool.tasx.model.company.project.task.Task;
import com.codecool.tasx.model.company.project.task.TaskDao;
import com.codecool.tasx.model.user.ApplicationUser;
import com.codecool.tasx.service.auth.UserProvider;
import com.codecool.tasx.service.converter.UserConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskRoleService {
  private final TaskDao taskDao;
  private final UserProvider userProvider;
  private final UserConverter userConverter;

  @Transactional(readOnly = true)
  @PreAuthorize("hasPermission(#taskId, 'Project', 'PROJECT_ASSIGNED_EMPLOYEE')")
  public List<UserResponsePublicDto> getAssignedEmployees(
    Long companyId, Long projectId, Long taskId) {
    Task task = taskDao.findByCompanyIdAndProjectIdAndTaskId(companyId, projectId, taskId)
      .orElseThrow(() -> new TaskNotFoundException(taskId));
    return userConverter.getUserResponsePublicDtos(task.getAssignedEmployees().stream().toList());
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#taskId, 'Project', 'PROJECT_ASSIGNED_EMPLOYEE')")
  public void assignSelf(Long companyId, Long projectId, Long taskId) {
    Task task = taskDao.findByCompanyIdAndProjectIdAndTaskId(companyId, projectId, taskId)
      .orElseThrow(() -> new TaskNotFoundException(taskId));
    ApplicationUser applicationUser = userProvider.getAuthenticatedUser();
    task.assignEmployee(applicationUser);
    taskDao.save(task);
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#taskId, 'Project', 'PROJECT_ASSIGNED_EMPLOYEE')")
  public void removeSelf(Long companyId, Long projectId, Long taskId) {
    Task task = taskDao.findByCompanyIdAndProjectIdAndTaskId(companyId, projectId, taskId)
      .orElseThrow(() -> new TaskNotFoundException(taskId));
    ApplicationUser applicationUser = userProvider.getAuthenticatedUser();
    task.removeEmployee(applicationUser);
    taskDao.save(task);
  }
}
