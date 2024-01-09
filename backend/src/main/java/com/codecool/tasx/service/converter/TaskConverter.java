package com.codecool.tasx.service.converter;

import com.codecool.tasx.controller.dto.company.project.task.TaskResponsePublicDto;
import com.codecool.tasx.model.company.project.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskConverter {
  private final UserConverter userConverter;

  @Autowired
  public TaskConverter(UserConverter userConverter) {
    this.userConverter = userConverter;
  }

  public TaskResponsePublicDto getTaskResponsePublicDto(Task task) {
    return new TaskResponsePublicDto(task.getProject().getId(),
      task.getId(), task.getName(), task.getDescription(), task.getImportance(),
      task.getDifficulty(),
      task.getStartDate(), task.getDeadline(), task.getTaskStatus(),
      userConverter.getUserResponsePublicDto(task.getTaskOwner()), task.getPoints());
  }

  public List<TaskResponsePublicDto> getTaskResponsePublicDtos(List<Task> tasks) {
    return tasks.stream().map(task -> getTaskResponsePublicDto(task)).collect(Collectors.toList());
  }
}
