package net.dnadas.auth.model.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationUserDao extends JpaRepository<ApplicationUser, Long> {
}
