package com.codecool.tasx.model.request;

import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.user.ApplicationUser;
import jakarta.persistence.*;
import lombok.*;

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

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "company_id")
  private Company company;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private ApplicationUser applicationUser;

  @Enumerated(EnumType.STRING)
  private RequestStatus status;

  public CompanyJoinRequest(Company company, ApplicationUser applicationUser) {
    this.company = company;
    this.applicationUser = applicationUser;
    this.status = RequestStatus.PENDING;
  }
}
