package net.dnadas.companies.model.request;

import jakarta.persistence.*;
import lombok.*;
import net.dnadas.companies.model.company.project.Project;

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
  @JoinColumn(name = "project_id", nullable = false)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Project project;

  @Column(nullable = false)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Long userId;

  @Enumerated(EnumType.STRING)
  private RequestStatus status;

  public ProjectJoinRequest(Project project, Long userId) {
    this.project = project;
    this.userId = userId;
    this.status = RequestStatus.PENDING;
  }
}
