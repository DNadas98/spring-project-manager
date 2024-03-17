package net.dnadas.monolith.model.company.project.task;

import net.dnadas.monolith.model.company.project.Project;
import net.dnadas.monolith.auth.model.user.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

  @Query(
    "SELECT t FROM Task t" +
      " WHERE t.project = :project" +
      " AND :user MEMBER OF t.assignedEmployees"
  )
  List<Task> findAllByProjectAndApplicationUser(
    @Param("project") Project project, @Param("user") ApplicationUser user);

  @Query(
    "SELECT t FROM Task t" +
      " WHERE t.project = :project" +
      " AND :user NOT MEMBER OF t.assignedEmployees"
  )
  List<Task> findAllByProjectAndWithoutApplicationUser(
    @Param("project") Project project, @Param("user") ApplicationUser user);

  @Query(
    "SELECT t FROM Task t" +
      " WHERE t.project = :project" +
      " AND t.taskStatus = :taskStatus" +
      " AND :user MEMBER OF t.assignedEmployees"
  )
  List<Task> findAllByProjectAndTaskStatusAndApplicationUser(
    Project project, TaskStatus taskStatus, ApplicationUser user);

  @Query(
    "SELECT t FROM Task t" +
      " WHERE t.project = :project" +
      " AND t.taskStatus = :taskStatus" +
      " AND :user NOT MEMBER OF t.assignedEmployees"
  )
  List<Task> findAllByProjectAndTaskStatusAndWithoutApplicationUser(
    @Param("project") Project project, @Param("taskStatus") TaskStatus taskStatus,
    @Param("user") ApplicationUser user);
}
