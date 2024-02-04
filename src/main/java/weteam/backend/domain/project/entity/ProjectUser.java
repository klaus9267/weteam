package weteam.backend.domain.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import weteam.backend.application.handler.exception.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.meeting.entity.MeetingUser;
import weteam.backend.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "project_users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ProjectUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;

    @Column(columnDefinition = "boolean default true")
    private boolean enable;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @JsonIgnore
    @OneToMany(mappedBy = "projectUser")
    private List<MeetingUser> meetingUserList = new ArrayList<>();

    public static ProjectUser from(final User user, final Project project) {
        return ProjectUser.builder().user(user).project(project).enable(true).build();
    }

    public static ProjectUser from(Project project, Long userId) {
        return ProjectUser.builder().project(project).user(new User(userId)).enable(true).build();
    }

    public void updateRole(final String role) {
        this.role = role;
    }

    public void disable() {
        if (!this.enable) {
            throw new CustomException(CustomErrorCode.BAD_REQUEST, "이미 탈퇴한 프로젝트입니다.");
        }
        if (this.getProject().getHost().getId().equals(this.user.getId())) {
            throw new CustomException(CustomErrorCode.BAD_REQUEST, "호스트를 넘기기전에 탈퇴할 수 없습니다.");
        }
        this.enable = !enable;
    }
}
