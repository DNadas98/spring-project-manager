package com.codecool.tasx.model.requests;

import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.user.ApplicationUser;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "project_join_request")
public class ProjectJoinRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "project_id")
  private Project project;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private ApplicationUser applicationUser;

  @Enumerated(EnumType.STRING)
  private RequestStatus status;

  public ProjectJoinRequest() {
  }

  public ProjectJoinRequest(Project project, ApplicationUser applicationUser) {
    this.project = project;
    this.applicationUser = applicationUser;
    this.status = RequestStatus.PENDING;
  }

  public Long getId() {
    return id;
  }

  public Project getProject() {
    return project;
  }

  public ApplicationUser getUser() {
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
    return Objects.hash(id, project, applicationUser, status);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ProjectJoinRequest that = (ProjectJoinRequest) object;
    return Objects.equals(id, that.id) && Objects.equals(project, that.project) && Objects.equals(
      applicationUser, that.applicationUser) && status == that.status;
  }

  @Override
  public String toString() {
    return "ProjectJoinRequest{" + "id=" + id + ", project=" + project + ", applicationUser=" +
      applicationUser +
      ", status=" + status + '}';
  }
}
