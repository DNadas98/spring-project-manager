package com.codecool.tasx.controller;

import com.codecool.tasx.dto.company.project.task.TaskCreateRequestDto;
import com.codecool.tasx.dto.company.project.task.TaskResponsePublicDto;
import com.codecool.tasx.dto.company.project.task.TaskUpdateRequestDto;
import com.codecool.tasx.model.company.project.task.TaskStatus;
import com.codecool.tasx.service.company.project.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/companies/{companyId}/projects/{projectId}/tasks")
@RequiredArgsConstructor
public class TaskController {
  private final TaskService taskService;

  @GetMapping
  public ResponseEntity<?> getAllTasks(
    @PathVariable Long companyId, @PathVariable Long projectId,
    @RequestParam(name = "taskStatus", required = false) TaskStatus taskStatus) {
    List<TaskResponsePublicDto> tasks;
    if (taskStatus != null) {
      tasks = taskService.getTasksByStatus(companyId, projectId,
        taskStatus);
    } else {
      tasks = taskService.getAllTasks(companyId, projectId);
    }
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", tasks));
  }

  @GetMapping("/{taskId}")
  public ResponseEntity<?> getTaskById(
    @PathVariable Long companyId, @PathVariable Long projectId, @PathVariable Long taskId) {
    TaskResponsePublicDto task = taskService.getTaskById(companyId, projectId, taskId);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", task));
  }

  @PostMapping
  public ResponseEntity<?> createTask(
    @PathVariable Long companyId, @PathVariable Long projectId,
    @RequestBody TaskCreateRequestDto taskDetails) {
    TaskResponsePublicDto taskResponseDetails = taskService.createTask(taskDetails, companyId,
      projectId);
    return ResponseEntity.status(HttpStatus.CREATED).body(
      Map.of("message", "Task created successfully", "data", taskResponseDetails));
  }

  @PutMapping("/{taskId}")
  public ResponseEntity<?> updateTask(
    @PathVariable Long companyId, @PathVariable Long projectId, @PathVariable Long taskId,
    @RequestBody TaskUpdateRequestDto taskDetails) {
    TaskResponsePublicDto taskResponseDetails = taskService.updateTask(taskDetails, companyId,
      projectId, taskId);

    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Task with ID " + taskId + " updated successfully", "data",
        taskResponseDetails));
  }

  @DeleteMapping("/{taskId}")
  public ResponseEntity<?> deleteTask(
    @PathVariable Long companyId, @PathVariable Long projectId, @PathVariable Long taskId) {
    taskService.deleteTask(companyId, projectId, taskId);

    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Task with ID " + taskId + " deleted successfully"));
  }
}
