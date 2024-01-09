package com.codecool.tasx.model.requests;

import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyJoinRequestDao extends JpaRepository<CompanyJoinRequest, Long> {
  @Query(
    "SELECT cjr FROM CompanyJoinRequest cjr WHERE cjr.id = :id AND cjr.company.id = :companyId")
  Optional<CompanyJoinRequest> findByIdAndCompanyId(Long id, Long companyId);

  List<CompanyJoinRequest> findByCompanyAndStatus(Company company, RequestStatus status);

  Optional<CompanyJoinRequest> findOneByCompanyAndUser(Company company, User user);

  List<CompanyJoinRequest> findByUser(User user);
}
