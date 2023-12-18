package weteam.backend.domain.project;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import weteam.backend.application.common.ApiMetaData;
import weteam.backend.application.security.SecurityUtil;
import weteam.backend.domain.project.dto.ProjectDto;
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
    @Operation(summary = "팀플 생성")
    @ApiResponse(responseCode = "201", useReturnTypeSchema = true)
    public ApiMetaData<ProjectDto> create(@RequestBody @Valid RequestProjectDto projectDto) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return new ApiMetaData<>(HttpStatus.CREATED, projectService.create(memberId, projectDto.toEntity()));
    }

    @GetMapping("{projectId}")
    @Operation(summary = "팀원 목록 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ApiMetaData<List<ProjectMemberDto>> findProjectMemberList(@PathVariable("projectId") Long projectId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return new ApiMetaData<>(projectService.findMemberListByProject(projectId));
    }

    @PatchMapping("{projectId}")
    @Operation(summary = "초대 수락")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ApiMetaData<?> acceptInvite(@PathVariable("projectId") Long projectId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        projectService.acceptInvite(projectId, memberId);
        return new ApiMetaData<>(HttpStatus.OK);
    }
}
