package weteam.backend.domain.profile;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import weteam.backend.application.swagger.SwaggerCreated;
import weteam.backend.application.swagger.SwaggerNoContent;
import weteam.backend.application.swagger.SwaggerOK;
import weteam.backend.domain.profile.dto.ProfileDto;
import weteam.backend.domain.user.dto.UserDto;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@Tag(name = "PFOFILE")
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping("{imageIdx}")
    @SwaggerCreated(summary = "프로필 사진 생성")
    public void addProfileImage(
            @PathVariable("imageIdx") final Long imageIdx,
            @AuthenticationPrincipal final UserDto user
    ) {
        profileService.addProfileImage(user.id(), imageIdx);
    }

    @GetMapping
    @SwaggerOK(summary = "프로필 사진 조회")
    public ResponseEntity<ProfileDto> readProfileImage(@AuthenticationPrincipal final UserDto user) {
        final ProfileDto profileDto = profileService.findProfileImage(user.id());
        return ResponseEntity.ok(profileDto);
    }

    @PatchMapping("{imageIdx}")
    @SwaggerNoContent(summary = "프로필 사진 변경")
    public void updateProfileImage(
            @PathVariable("imageIdx") final Long imageIdx,
            @AuthenticationPrincipal final UserDto user
    ) {
        profileService.updateProfileImage(user.id(), imageIdx);
    }
}
