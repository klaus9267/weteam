package weteam.backend.domain.profile;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import weteam.backend.application.swagger.SwaggerCreated;
import weteam.backend.application.swagger.SwaggerNoContent;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@Tag(name = "PROFILE")
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping("{imageIdx}")
    @SwaggerCreated(summary = "프로필 사진 생성")
    @ResponseStatus(HttpStatus.CREATED)
    public void addProfileImage(@PathVariable("imageIdx") final Long imageIdx) {
        profileService.addProfileImage(imageIdx);
    }

    @PatchMapping("{imageIdx}")
    @SwaggerNoContent(summary = "프로필 사진 변경")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProfileImage(@PathVariable("imageIdx") final Long imageIdx) {
        profileService.updateProfileImage(imageIdx);
    }
}
