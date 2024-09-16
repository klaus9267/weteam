package weteam.backend.domain.project;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import weteam.backend.domain.common.pagination.param.ProjectPaginationParam;
import weteam.backend.domain.common.swagger.SwaggerCreated;
import weteam.backend.domain.common.swagger.SwaggerNoContent;
import weteam.backend.domain.common.swagger.SwaggerOK;
import weteam.backend.domain.project.dto.CreateProjectDto;
import weteam.backend.domain.project.dto.ProjectDto;
import weteam.backend.domain.project.dto.ProjectPaginationDto;
import weteam.backend.domain.project.dto.UpdateProjectDto;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "PROJECT")
public class ProjectController {
  private final ProjectFacade projectFacade;

  @PostMapping
  @SwaggerCreated(summary = "팀플 생성")
  @ResponseStatus(HttpStatus.CREATED)
  public void addProject(@RequestBody @Valid final CreateProjectDto createProjectDto) {
    projectFacade.addProject(createProjectDto);
  }

  @GetMapping
  @SwaggerOK(summary = "팀플 목록 조회", description = "done으로 종료, 진행 분류해서 조회")
  @PageableAsQueryParam
  public ResponseEntity<ProjectPaginationDto> readProjectList(@ParameterObject @Valid final ProjectPaginationParam paginationParam) {
    final ProjectPaginationDto paginationDto = projectFacade.pagingProjects(paginationParam);
    return ResponseEntity.ok(paginationDto);
  }

  @GetMapping("{projectId}")
  @SwaggerOK(summary = "팀플 단건 조회")
  public ResponseEntity<ProjectDto> readProject(@PathVariable("projectId") final Long projectId) {
    final ProjectDto projectDto = projectFacade.findProjectInfo(projectId);
    return ResponseEntity.ok(projectDto);
  }

  @PatchMapping("{projectId}/done")
  @Operation(summary = "팀플 진행 상황 변경")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateDone(@PathVariable("projectId") final Long projectId) {
    projectFacade.toggleIsDone(projectId);
  }

  @PatchMapping("{projectId}")
  @Operation(summary = "팀플 수정")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateProject(
      @PathVariable("projectId") final Long projectId,
      @RequestBody @Valid final UpdateProjectDto projectDto
  ) {
    projectFacade.updateProject(projectDto, projectId);
  }

  @PatchMapping("{projectId}/{userId}")
  @Operation(summary = "호스트 넘기기")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateHost(
      @PathVariable("projectId") final Long projectId,
      @PathVariable("userId") final Long userId
  ) {
    projectFacade.updateHost(projectId, userId);
  }

  @DeleteMapping("{projectId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @SwaggerNoContent(summary = "팀플 삭제")
  public void removeProject(@PathVariable("projectId") final Long projectId) {
    projectFacade.deleteProject(projectId);
  }
}
