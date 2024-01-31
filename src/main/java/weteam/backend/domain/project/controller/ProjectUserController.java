package weteam.backend.domain.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import weteam.backend.application.swagger.SwaggerNoContent;
import weteam.backend.application.swagger.SwaggerOK;
import weteam.backend.domain.project.dto.ProjectUserDto;
import weteam.backend.domain.project.param.UpdateProjectRoleParam;
import weteam.backend.domain.project.service.ProjectUserService;

import java.util.List;

@RestController
@RequestMapping("/api/project-users")
@RequiredArgsConstructor
@Tag(name = "PROJECT_USER")
public class ProjectUserController {
    private final ProjectUserService projectUserService;

    @GetMapping("{projectId}")
    @SwaggerOK(summary = "팀원 목록 조회")
    public ResponseEntity<List<ProjectUserDto>> findProjectMemberList(@PathVariable("projectId") final Long projectId) {
        return ResponseEntity.ok(projectUserService.findUsersByProjectId(projectId));
    }

    @PatchMapping
    @Operation(summary = "담당 역할 변경", description = "응답 없음")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProjectRole(@ParameterObject @Valid UpdateProjectRoleParam updateProjectRoleParam) {
        projectUserService.updateProjectRole(updateProjectRoleParam);
    }

    @PatchMapping("{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SwaggerNoContent(summary = "초대 수락", description = "응답 없음")
    public void acceptInvite(@PathVariable("projectId") final Long projectId) {
        projectUserService.acceptInvite(projectId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SwaggerNoContent(summary = "팀원 강퇴", description = "응답 없음")
    public void kickUser(@NotNull @RequestParam("projectUserIdList") final List<Long> projectUserIdList) {
        projectUserService.kickUsers(projectUserIdList);
    }

    @DeleteMapping("{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SwaggerNoContent(summary = "팀플 탈퇴", description = "응답 없음")
    public void exitProject(@PathVariable("projectId") final Long projectId) {
        projectUserService.exitProject(projectId);
    }
}
