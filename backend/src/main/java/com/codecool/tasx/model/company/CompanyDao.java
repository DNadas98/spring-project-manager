package com.codecool.tasx.model.company;

import com.codecool.tasx.model.requests.RequestStatus;
import com.codecool.tasx.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyDao extends JpaRepository<Company, Long> {

  //https://www.baeldung.com/spring-data-jpa-query

  @Query("SELECT c FROM Company c WHERE :user MEMBER OF c.employees")
  List<Company> findAllWithEmployee(@Param("user") User user);

  /*@Query("SELECT c FROM Company c WHERE :user NOT MEMBER OF c.employees")
  List<Company> findAllWithoutEmployee(@Param("user") User user);*/

  @Query("SELECT c FROM Company c WHERE :user NOT MEMBER OF c.employees AND c.id NOT IN " +
    "(SELECT cr.company.id FROM CompanyJoinRequest cr " +
    "WHERE cr.user = :user AND cr.status IN (:statuses))")
  List<Company> findAllWithoutEmployeeAndJoinRequest(
    @Param("user") User user,
    @Param("statuses") List<RequestStatus> statuses);
}
