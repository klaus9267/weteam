package weteam.backend.domain.project;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import weteam.backend.application.common.ApiMetaData;
import weteam.backend.application.oauth.PrincipalDetails;
import weteam.backend.domain.project.dto.ProjectMemberDto;
import weteam.backend.domain.project.dto.RequestProjectDto;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Project")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    @Operation(summary = "팀플 생성", description = "응답 없음")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(
            @RequestBody @Valid RequestProjectDto projectDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        projectService.create(principalDetails.getMember().id(), projectDto.toEntity());
    }

    @GetMapping("{projectId}")
    @Operation(summary = "팀원 목록 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ApiMetaData<List<ProjectMemberDto>> findProjectMemberList(@PathVariable("projectId") Long projectId) {
        return new ApiMetaData<>(projectService.findMemberListByProject(projectId));
    }

    @PatchMapping("{projectId}")
    @Operation(summary = "초대 수락", description = "응답 없음")
    @ResponseStatus(HttpStatus.OK)
    public void acceptInvite(
            @PathVariable("projectId") Long projectId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        projectService.acceptInvite(projectId, principalDetails.getMember().id());
    }
}
