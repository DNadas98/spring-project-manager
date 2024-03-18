package net.dnadas.monolith.model.request;

import net.dnadas.monolith.model.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyJoinRequestDao extends JpaRepository<CompanyJoinRequest, Long> {
  @Query(
    "SELECT cjr FROM CompanyJoinRequest cjr WHERE cjr.id = :id AND cjr.company.id = :companyId")
  Optional<CompanyJoinRequest> findByIdAndCompanyId(
    @Param("id") Long id, @Param("companyId") Long companyId);

  List<CompanyJoinRequest> findByCompanyAndStatus(Company company, RequestStatus status);

  Optional<CompanyJoinRequest> findOneByCompanyAndUserId(
    Company company, Long userId);

  Optional<CompanyJoinRequest> findByIdAndUserId(Long id, Long userId);

  List<CompanyJoinRequest> findByUserId(Long userId);

  @Override
  @Transactional
  @Modifying
  @Query("delete from CompanyJoinRequest c where c.id = :id")
  void deleteById(@Param("id") Long id);
}
