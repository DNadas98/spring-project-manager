package net.dnadas.companies.model.request;

import jakarta.persistence.*;
import lombok.*;
import net.dnadas.companies.model.company.Company;

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
  @JoinColumn(name = "company_id", nullable = false)
  @ToString.Exclude
  private Company company;

  @ToString.Exclude
  @Column(nullable = false)
  private Long userId;

  @Enumerated(EnumType.STRING)
  private RequestStatus status;

  public CompanyJoinRequest(Company company, Long userId) {
    this.company = company;
    this.userId = userId;
    this.status = RequestStatus.PENDING;
  }
}
