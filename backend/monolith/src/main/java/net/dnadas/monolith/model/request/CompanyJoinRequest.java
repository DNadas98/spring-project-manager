package net.dnadas.monolith.model.request;

import jakarta.persistence.*;
import lombok.*;
import net.dnadas.monolith.model.company.Company;
import net.dnadas.monolith.auth.model.user.ApplicationUser;

@Entity
@Table(name = "company_join_request")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CompanyJoinRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "company_id")
  @ToString.Exclude
  private Company company;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @ToString.Exclude
  private ApplicationUser applicationUser;

  @Enumerated(EnumType.STRING)
  private RequestStatus status;

  public CompanyJoinRequest(Company company, ApplicationUser applicationUser) {
    this.company = company;
    this.applicationUser = applicationUser;
    this.status = RequestStatus.PENDING;
  }
}
