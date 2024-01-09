package com.codecool.tasx.model.company.project.task;

import com.codecool.tasx.model.company.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TaskDao extends JpaRepository<Task, Long> {
  @Query(
    "SELECT t FROM Task t WHERE t.project.company.id = :companyId" +
      " AND t.project.id = :projectId" +
      " AND t.id = :taskId")
  Optional<Task> findByCompanyIdAndProjectIdAndTaskId(
    @Param("companyId") Long companyId,
    @Param("projectId") Long projectId,
    @Param("taskId") Long taskId);

  List<Task> findAllByProjectAndTaskStatus(Project project, TaskStatus taskStatus);

  List<Task> findAllByProjectAndTaskStatusIn(Project project, Set<TaskStatus> taskStatuses);

  List<Task> findAllByProjectAndTaskStatusNotIn(Project project, Set<TaskStatus> taskStatuses);
}
