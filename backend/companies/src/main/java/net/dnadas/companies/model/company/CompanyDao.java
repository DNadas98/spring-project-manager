package net.dnadas.companies.model.company;

import net.dnadas.companies.model.request.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyDao extends JpaRepository<Company, Long> {

  //https://www.baeldung.com/spring-data-jpa-query

  @Query("SELECT c FROM Company c WHERE :userId MEMBER OF c.employees")
  List<Company> findAllWithEmployee(@Param("userId") Long userId);

  @Query(
    "SELECT c FROM Company c WHERE :userId NOT MEMBER OF c.employees AND c.id NOT IN " +
      "(SELECT cr.company.id FROM CompanyJoinRequest cr " +
      "WHERE cr.userId = :userId AND cr.status IN (:statuses))")
  List<Company> findAllWithoutEmployeeAndJoinRequest(
    @Param("userId") Long userId,
    @Param("statuses") List<RequestStatus> statuses);
}
