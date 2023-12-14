package weteam.backend.domain.project;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import weteam.backend.application.common.ApiMetaData;
import weteam.backend.domain.project.domain.Project;
import weteam.backend.domain.project.dto.ProjectDto;
import weteam.backend.domain.project.mapper.ProjectMapper;
import weteam.backend.application.security.SecurityUtil;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Group Project", description = "group project API")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping("")
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "팀플 생성", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    public ApiMetaData<ProjectDto.Res> createProject(@RequestBody @Valid ProjectDto.Create request) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Project entity = ProjectMapper.instance.toEntity(request);
        ProjectDto.Res res= projectService.createProject(memberId, entity);
        return new ApiMetaData<>(HttpStatus.CREATED, res);
    }

    @GetMapping("/invite/{projectId}")
    public ApiMetaData<?> acceptInvite(@PathVariable("projectId") Long projectId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ApiMetaData.builder()
                          .result(true)
                          .httpStatus(HttpStatus.OK)
                          .message("팀플 초대 성공")
                          .build();
    }
}
