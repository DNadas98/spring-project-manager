package net.dnadas.monolith.auth.model.account;

import net.dnadas.monolith.auth.model.user.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserAccountDao extends JpaRepository<UserAccount, Long> {
  Optional<UserAccount> findOneByEmailAndAccountType(String email, AccountType accountType);

  Set<UserAccount> findAllByEmail(String email);

  Set<UserAccount> findAllByApplicationUser(ApplicationUser applicationUser);


  Optional<UserAccount> findByIdAndApplicationUser(Long id, ApplicationUser applicationUser);

  @Modifying
  @Query("DELETE FROM UserAccount u WHERE u.id = :id")
  void deleteOneById(@Param("id") Long id);
}
