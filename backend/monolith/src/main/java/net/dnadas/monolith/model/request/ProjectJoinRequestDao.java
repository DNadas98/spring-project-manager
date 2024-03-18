package net.dnadas.monolith.model.request;

import net.dnadas.monolith.model.company.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectJoinRequestDao extends JpaRepository<ProjectJoinRequest, Long> {
  @Query("SELECT pjr FROM ProjectJoinRequest pjr" + " WHERE pjr.project.company.id = :companyId" +
    " AND pjr.project.id = :projectId" + " AND pjr.id = :requestId")
  Optional<ProjectJoinRequest> findByCompanyIdAndProjectIdAndRequestId(
    @Param("companyId") Long companyId, @Param("projectId") Long projectId,
    @Param("requestId") Long requestId);

  List<ProjectJoinRequest> findByProjectAndStatus(Project project, RequestStatus status);

  Optional<ProjectJoinRequest> findByProjectAndUserId(
    Project project, Long userId);

  Optional<ProjectJoinRequest> findByIdAndUserId(Long id, Long userId);

  List<ProjectJoinRequest> findByUserId(Long userId);
}
