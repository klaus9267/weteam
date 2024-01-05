package weteam.backend.domain.project;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import weteam.backend.application.oauth.PrincipalDetails;
import weteam.backend.application.swagger.SwaggerCreated;
import weteam.backend.domain.project.dto.CreateProjectDto;
import weteam.backend.domain.project.dto.ProjectMemberDto;
import weteam.backend.domain.project.dto.ProjectPaginationDto;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "PROJECT")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    @SwaggerCreated(summary = "팀플 생성")
    public void createProject(
            @RequestBody @Valid CreateProjectDto projectDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        projectService.save(principalDetails.getUser().id(), projectDto);
    }

    @GetMapping
    @Operation(summary = "팀플 목록 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    @PageableAsQueryParam
    public ResponseEntity<ProjectPaginationDto> readProjectList(
            @Parameter(hidden = true)
            Pageable pageable, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.ok(projectService.findProjects(principalDetails.getUser().id(), pageable));
    }

    @GetMapping("{projectId}")
    @Operation(summary = "팀원 목록 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ResponseEntity<List<ProjectMemberDto>> findProjectMemberList(Pageable pageable, @PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(projectService.findMemberListByProject(projectId));
    }

    @PatchMapping("{projectId}")
    @Operation(summary = "초대 수락", description = "응답 없음")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void acceptInvite(
            @PathVariable("projectId") Long projectId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        projectService.acceptInvite(projectId, principalDetails.getUser().id());
    }
}
