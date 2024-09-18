package weteam.backend.domain.profile.dto;

import lombok.Builder;
import weteam.backend.domain.profile.entity.ProfileImage;

@Builder
public record ProfileDto(
        Long userId,
        Long imageIdx
) {
    public static ProfileDto from(final ProfileImage profileImage) {
        return ProfileDto.builder().userId(profileImage.getUser().getId()).imageIdx(profileImage.getImageIdx()).build();
    }
}
