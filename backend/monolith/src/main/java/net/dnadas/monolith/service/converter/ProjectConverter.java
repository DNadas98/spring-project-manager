package net.dnadas.monolith.service.converter;

import lombok.RequiredArgsConstructor;
import net.dnadas.monolith.auth.service.user.UserConverter;
import net.dnadas.monolith.dto.company.project.ProjectResponsePrivateDTO;
import net.dnadas.monolith.dto.company.project.ProjectResponsePublicDTO;
import net.dnadas.monolith.dto.requests.ProjectJoinRequestResponseDto;
import net.dnadas.monolith.model.company.project.Project;
import net.dnadas.monolith.model.request.ProjectJoinRequest;
import net.dnadas.monolith.service.datetime.DateTimeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectConverter {
  private final UserConverter userConverter;
  private final TaskConverter taskConverter;
  private final DateTimeService dateTimeService;

  public ProjectResponsePublicDTO getProjectResponsePublicDto(Project project) {
    return new ProjectResponsePublicDTO(project.getCompany().getId(), project.getId(),
      project.getName(), project.getDescription());
  }

  public ProjectResponsePrivateDTO getProjectResponsePrivateDto(Project project) {
    return new ProjectResponsePrivateDTO(project.getCompany().getId(), project.getId(),
      project.getName(), project.getDescription(),
      dateTimeService.toDisplayedDate(project.getStartDate()),
      dateTimeService.toDisplayedDate(project.getDeadline()),
      taskConverter.getTaskResponsePublicDtos(project.getTasks().stream().toList()));
  }

  public List<ProjectResponsePublicDTO> getProjectResponsePublicDtos(List<Project> projects) {
    return projects.stream().map(
      project -> getProjectResponsePublicDto(project)).collect(Collectors.toList());
  }

  public ProjectJoinRequestResponseDto getProjectJoinRequestResponseDto(
    ProjectJoinRequest request) {
    return new ProjectJoinRequestResponseDto(request.getId(),
      getProjectResponsePublicDto(request.getProject()),
      userConverter.getUserResponsePublicDto(request.getApplicationUser()), request.getStatus());
  }

  public List<ProjectJoinRequestResponseDto> getProjectJoinRequestResponseDtos(
    List<ProjectJoinRequest> requests) {
    return requests.stream().map(request -> getProjectJoinRequestResponseDto(request)).collect(
      Collectors.toList());
  }
}
