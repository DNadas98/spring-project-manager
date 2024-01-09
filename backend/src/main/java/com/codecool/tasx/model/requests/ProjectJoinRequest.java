package com.codecool.tasx.model.requests;

import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.user.User;
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
  private User user;

  @Enumerated(EnumType.STRING)
  private RequestStatus status;

  public ProjectJoinRequest() {
  }

  public ProjectJoinRequest(Project project, User user) {
    this.project = project;
    this.user = user;
    this.status = RequestStatus.PENDING;
  }

  public Long getId() {
    return id;
  }

  public Project getProject() {
    return project;
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
    return Objects.hash(id, project, user, status);
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
      user, that.user) && status == that.status;
  }

  @Override
  public String toString() {
    return "ProjectJoinRequest{" + "id=" + id + ", project=" + project + ", user=" + user +
      ", status=" + status + '}';
  }
}
