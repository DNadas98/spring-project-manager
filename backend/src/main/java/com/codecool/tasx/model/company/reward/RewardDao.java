package com.codecool.tasx.model.company.reward;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RewardDao extends JpaRepository<Reward, Long> {
  @Query(
    "SELECT r FROM Reward r WHERE r.id = :rewardId" +
      " AND r.company.id = :companyId")
  Optional<Reward> findByIdAndCompanyId(
    @Param("rewardId") Long rewardId, @Param("companyId") Long companyId);

}
