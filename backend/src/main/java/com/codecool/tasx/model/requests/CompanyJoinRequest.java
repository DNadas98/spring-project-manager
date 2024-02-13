package com.codecool.tasx.model.requests;

import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.user.ApplicationUser;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "company_join_request")
public class CompanyJoinRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "company_id")
  private Company company;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private ApplicationUser applicationUser;

  @Enumerated(EnumType.STRING)
  private RequestStatus status;

  public CompanyJoinRequest() {
  }

  public CompanyJoinRequest(Company company, ApplicationUser applicationUser) {
    this.company = company;
    this.applicationUser = applicationUser;
    this.status = RequestStatus.PENDING;
  }

  public Long getId() {
    return id;
  }

  public Company getCompany() {
    return company;
  }

  public ApplicationUser getApplicationUser() {
    return applicationUser;
  }

  public RequestStatus getStatus() {
    return status;
  }

  public void setStatus(RequestStatus status) {
    this.status = status;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, company, applicationUser, status);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    CompanyJoinRequest that = (CompanyJoinRequest) object;
    return Objects.equals(id, that.id) && Objects.equals(company, that.company) && Objects.equals(
      applicationUser, that.applicationUser) && status == that.status;
  }

  @Override
  public String toString() {
    return "CompanyJoinRequest{" + "id=" + id + ", company=" + company + ", populate=" +
      applicationUser +
      ", status=" + status + '}';
  }
}
