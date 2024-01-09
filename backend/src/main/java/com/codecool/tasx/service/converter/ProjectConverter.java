package com.codecool.tasx.service.converter;

import com.codecool.tasx.controller.dto.company.project.ProjectResponsePrivateDTO;
import com.codecool.tasx.controller.dto.company.project.ProjectResponsePublicDTO;
import com.codecool.tasx.controller.dto.requests.ProjectJoinRequestResponseDto;
import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.requests.ProjectJoinRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectConverter {
  private final UserConverter userConverter;
  private final TaskConverter taskConverter;

  public ProjectConverter(UserConverter userConverter, TaskConverter taskConverter) {
    this.userConverter = userConverter;
    this.taskConverter = taskConverter;
  }

  public ProjectResponsePublicDTO getProjectResponsePublicDto(Project project) {
    return new ProjectResponsePublicDTO(project.getCompany().getId(), project.getId(),
      project.getName(), project.getDescription());
  }

  public ProjectResponsePrivateDTO getProjectResponsePrivateDto(Project project) {
    return new ProjectResponsePrivateDTO(project.getCompany().getId(), project.getId(),
      project.getName(),
      project.getDescription(), project.getStartDate(), project.getDeadline(),
      userConverter.getUserResponsePublicDto(project.getProjectOwner()),
      userConverter.getUserResponsePublicDtos(project.getAssignedEmployees()),
      taskConverter.getTaskResponsePublicDtos(project.getTasks()));
  }

  public List<ProjectResponsePublicDTO> getProjectResponsePublicDtos(List<Project> projects) {
    return projects.stream().map(
      project -> getProjectResponsePublicDto(project)).collect(Collectors.toList());
  }

  public List<ProjectResponsePrivateDTO> getProjectResponsePrivateDtos(List<Project> projects) {
    return projects.stream().map(this::getProjectResponsePrivateDto).toList();
  }

  public ProjectJoinRequestResponseDto getProjectJoinRequestResponseDto(
    ProjectJoinRequest request) {
    return new ProjectJoinRequestResponseDto(request.getId(),
      getProjectResponsePublicDto(request.getProject()),
      userConverter.getUserResponsePublicDto(request.getUser()), request.getStatus());
  }

  public List<ProjectJoinRequestResponseDto> getProjectJoinRequestResponseDtos(
    List<ProjectJoinRequest> requests) {
    return requests.stream().map(request -> getProjectJoinRequestResponseDto(request)).collect(
      Collectors.toList());
  }
}
