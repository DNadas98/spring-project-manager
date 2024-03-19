package net.dnadas.companies.service.company.project.task;

import lombok.RequiredArgsConstructor;
import net.dnadas.auth.dto.authentication.UserInfoDto;
import net.dnadas.auth.dto.user.UserResponsePublicDto;
import net.dnadas.companies.exception.company.project.task.TaskNotFoundException;
import net.dnadas.companies.model.authorization.PermissionType;
import net.dnadas.companies.model.company.project.task.Task;
import net.dnadas.companies.model.company.project.task.TaskDao;
import net.dnadas.companies.service.authorization.CustomPermissionEvaluator;
import net.dnadas.companies.service.user.UserProvider;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskRoleService {
  private final TaskDao taskDao;
  private final UserProvider userProvider;
  private final CustomPermissionEvaluator permissionEvaluator;

  @Transactional(readOnly = true)
  public Set<PermissionType> getUserPermissionsForTask(
    Long companyId, Long projectId, Long taskId) {
    Task task = taskDao.findByCompanyIdAndProjectIdAndTaskId(companyId, projectId, taskId)
      .orElseThrow(() -> new TaskNotFoundException(taskId));
    UserInfoDto userInfo = userProvider.getUserInfo();
    Set<PermissionType> permissions = new HashSet<>();
    if (permissionEvaluator.hasTaskAssignedEmployeeAccess(userInfo, task)) {
      permissions.add(PermissionType.TASK_ASSIGNED_EMPLOYEE);
    }
    return permissions;
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasPermission(#taskId, 'Project', 'PROJECT_ASSIGNED_EMPLOYEE')")
  public List<UserResponsePublicDto> getAssignedEmployees(
    Long companyId, Long projectId, Long taskId) {
    Task task = taskDao.findByCompanyIdAndProjectIdAndTaskId(companyId, projectId, taskId)
      .orElseThrow(() -> new TaskNotFoundException(taskId));
    Set<Long> assignedEmployees = task.getAssignedEmployees();
    //TODO: implement
    return List.of();
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#taskId, 'Project', 'PROJECT_ASSIGNED_EMPLOYEE')")
  public void assignSelf(Long companyId, Long projectId, Long taskId) {
    Task task = taskDao.findByCompanyIdAndProjectIdAndTaskId(companyId, projectId, taskId)
      .orElseThrow(() -> new TaskNotFoundException(taskId));
    Long userId = userProvider.getAuthenticatedUserId();
    task.assignEmployee(userId);
    taskDao.save(task);
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#taskId, 'Project', 'PROJECT_ASSIGNED_EMPLOYEE')")
  public void removeSelf(Long companyId, Long projectId, Long taskId) {
    Task task = taskDao.findByCompanyIdAndProjectIdAndTaskId(companyId, projectId, taskId)
      .orElseThrow(() -> new TaskNotFoundException(taskId));
    Long userId = userProvider.getAuthenticatedUserId();
    task.removeEmployee(userId);
    taskDao.save(task);
  }
}
