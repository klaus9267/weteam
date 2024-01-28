package weteam.backend.domain.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import weteam.backend.application.swagger.SwaggerCreated;
import weteam.backend.application.swagger.SwaggerNoContent;
import weteam.backend.domain.common.pagination.param.ProjectPaginationParam;
import weteam.backend.domain.project.dto.CreateProjectDto;
import weteam.backend.domain.project.dto.ProjectPaginationDto;
import weteam.backend.domain.project.dto.UpdateProjectDto;
import weteam.backend.domain.project.service.ProjectService;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "PROJECT")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    @SwaggerCreated(summary = "팀플 생성")
    public void addProject(@RequestBody @Valid final CreateProjectDto projectDto) {
        projectService.addProject(projectDto);
    }

    @GetMapping
    @Operation(summary = "팀플 목록 조회", description = "done으로 종료, 진행 분류해서 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    @PageableAsQueryParam
    public ResponseEntity<ProjectPaginationDto> readProjectList(@ParameterObject @Valid final ProjectPaginationParam paginationParam) {
        final ProjectPaginationDto paginationDto = projectService.findProjects(paginationParam);

        return ResponseEntity.ok(paginationDto);
    }

    @PatchMapping("{projectId}/done")
    @Operation(summary = "팀플 진행 상황 변경")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDone(@PathVariable("projectId") final Long projectId) {
        projectService.updateDone(projectId);
    }

    @PatchMapping("{projectId}")
    @Operation(summary = "팀플 수정")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProject(
            @PathVariable("projectId") final Long projectId,
            @RequestBody @Valid final UpdateProjectDto projectDto
    ) {
        projectService.updateProject(projectDto, projectId);
    }

    @PatchMapping("{projectId}/{userId}")
    @Operation(summary = "호스트 넘기기")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateHost(
            @PathVariable("projectId") final Long projectId,
            @PathVariable("userId") final Long userId
    ) {
        projectService.updateHost(projectId, userId);
    }

    @DeleteMapping("{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SwaggerNoContent(summary = "팀플 삭제")
    public void removeProject(@PathVariable("projectId") final Long projectId) {
        projectService.deleteProject(projectId);
    }
}
