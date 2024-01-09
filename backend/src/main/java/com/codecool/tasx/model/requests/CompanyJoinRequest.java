package com.codecool.tasx.model.requests;

import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.user.User;
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
  private User user;

  @Enumerated(EnumType.STRING)
  private RequestStatus status;

  public CompanyJoinRequest() {
  }

  public CompanyJoinRequest(Company company, User user) {
    this.company = company;
    this.user = user;
    this.status = RequestStatus.PENDING;
  }

  public Long getId() {
    return id;
  }

  public Company getCompany() {
    return company;
  }

  public User getUser() {
    return user;
  }

  public RequestStatus getStatus() {
    return status;
  }

  public void setStatus(RequestStatus status) {
    this.status = status;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, company, user, status);
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
      user, that.user) && status == that.status;
  }

  @Override
  public String toString() {
    return "CompanyJoinRequest{" + "id=" + id + ", company=" + company + ", populate=" + user +
      ", status=" + status + '}';
  }
}
