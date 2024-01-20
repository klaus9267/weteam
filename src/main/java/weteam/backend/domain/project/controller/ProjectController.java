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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import weteam.backend.application.swagger.SwaggerCreated;
import weteam.backend.domain.common.pagination.param.ProjectPaginationParam;
import weteam.backend.domain.project.dto.CreateProjectDto;
import weteam.backend.domain.project.dto.ProjectPaginationDto;
import weteam.backend.domain.project.dto.UpdateProjectDto;
import weteam.backend.domain.project.service.ProjectService;
import weteam.backend.domain.user.dto.UserDto;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "PROJECT")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    @SwaggerCreated(summary = "팀플 생성")
    public void addProject(
            @RequestBody @Valid final CreateProjectDto projectDto,
            @AuthenticationPrincipal final UserDto user
    ) {
        projectService.addProject(user.id(), projectDto);
    }

    @GetMapping
    @Operation(summary = "팀플 목록 조회 | done으로 종료, 진행 분류해서 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    @PageableAsQueryParam
    public ResponseEntity<ProjectPaginationDto> readProjectList(
            @ParameterObject @Valid final ProjectPaginationParam paginationParam,
            @AuthenticationPrincipal final UserDto user
    ) {
        final ProjectPaginationDto paginationDto = projectService.findProjects(user.id(), paginationParam);

        return ResponseEntity.ok(paginationDto);
    }

    @PatchMapping("{projectId}/done")
    @Operation(summary = "팀플 진행 상황 변경")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDone(
            @PathVariable("projectId") final Long projectId,
            @AuthenticationPrincipal final UserDto user
    ) {
        projectService.updateDone(projectId, user.id());
    }

    @PatchMapping("{projectId}")
    @Operation(summary = "팀플 수정")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProject(
            @PathVariable("projectId") final Long projectId,
            @RequestBody @Valid final UpdateProjectDto projectDto,
            @AuthenticationPrincipal final UserDto user
    ) {
        projectService.updateProject(projectDto, projectId, user.id());
    }

}
