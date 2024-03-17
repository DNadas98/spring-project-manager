package net.dnadas.monolith.model.request;

import jakarta.persistence.*;
import lombok.*;
import net.dnadas.monolith.model.company.project.Project;
import net.dnadas.monolith.model.user.ApplicationUser;

@Entity
@Table(name = "project_join_request")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ProjectJoinRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "project_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Project project;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private ApplicationUser applicationUser;

  @Enumerated(EnumType.STRING)
  private RequestStatus status;

  public ProjectJoinRequest(Project project, ApplicationUser applicationUser) {
    this.project = project;
    this.applicationUser = applicationUser;
    this.status = RequestStatus.PENDING;
  }
}
