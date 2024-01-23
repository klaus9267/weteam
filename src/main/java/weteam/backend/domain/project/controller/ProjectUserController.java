package weteam.backend.domain.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import weteam.backend.application.swagger.SwaggerNoContent;
import weteam.backend.application.swagger.SwaggerOK;
import weteam.backend.domain.project.dto.ProjectMemberDto;
import weteam.backend.domain.project.param.UpdateProjectRoleParam;
import weteam.backend.domain.project.service.ProjectUserService;
import weteam.backend.domain.user.dto.UserDto;

import java.util.List;

@RestController
@RequestMapping("/api/project-users")
@RequiredArgsConstructor
@Tag(name = "PROJECT_USER")
public class ProjectUserController {
    private final ProjectUserService projectUserService;

    @GetMapping("{projectId}")
    @SwaggerOK(summary = "팀원 목록 조회")
    public ResponseEntity<List<ProjectMemberDto>> findProjectMemberList(@PathVariable("projectId") final Long projectId) {
        return ResponseEntity.ok(projectUserService.findUsersByProjectId(projectId));
    }

    @PatchMapping
    @Operation(summary = "담당 역할 변경", description = "응답 없음")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProjectRole(
            @ParameterObject @Valid UpdateProjectRoleParam updateProjectRoleParam,
            @AuthenticationPrincipal final UserDto user
    ) {
        projectUserService.updateProjectRole(updateProjectRoleParam, user.id());
    }

    @PatchMapping("{projectId}")
    @SwaggerNoContent(summary = "초대 수락", description = "응답 없음")
    public void acceptInvite(
            @PathVariable("projectId") final Long projectId,
            @AuthenticationPrincipal final UserDto user
    ) {
        projectUserService.acceptInvite(projectId, user.id());
    }

    @DeleteMapping("{userId}")
    @SwaggerNoContent(summary = "팀원 강퇴", description = "응답 없음")
    public void kickUser(
            @PathVariable("userId") final Long userId,
            @AuthenticationPrincipal final UserDto user
    ) {
        projectUserService.kickUser(user.id(), userId);
    }
}
