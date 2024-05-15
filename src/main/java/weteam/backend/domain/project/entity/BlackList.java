package weteam.backend.domain.project.entity;

import jakarta.persistence.*;
import lombok.*;
import weteam.backend.domain.user.entity.User;

@Entity(name = "black_list")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BlackList {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  private Project project;

  private BlackList(final ProjectUser projectUser) {
    this.user = projectUser.getUser();
    this.project = projectUser.getProject();
  }

  public static BlackList from(final ProjectUser projectUser) {
    return new BlackList(projectUser);
  }
}
