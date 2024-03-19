package net.dnadas.companies.dto.company.project;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import net.dnadas.companies.dto.company.project.task.TaskResponsePublicDto;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record ProjectResponsePrivateDTO(
  @NotNull @Min(1) Long companyId,
  @NotNull @Min(1) Long projectId,
  @NotNull @Length(min = 1, max = 50) String name,
  @NotNull @Length(min = 1, max = 500) String description,
  @NotNull String startDate,
  @NotNull String deadline,
  @NotNull List<@Valid TaskResponsePublicDto> tasks) {
}
