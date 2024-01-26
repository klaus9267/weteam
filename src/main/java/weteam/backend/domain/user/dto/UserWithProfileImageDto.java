package weteam.backend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import weteam.backend.domain.profile.ProfileImage;
import weteam.backend.domain.profile.dto.ProfileDto;
import weteam.backend.domain.user.entity.User;
import weteam.backend.domain.user.entity.UserRole;

@Getter
@AllArgsConstructor
public class UserWithProfileImageDto {
    private final Long id;
    private final String username;
    private final String email;
    private final String organization;
    private final UserRole role;
    private final ProfileDto profile;

    public UserWithProfileImageDto(final User user, final ProfileImage profileImage) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.organization = user.getOrganization();
        this.role = user.getRole();
        this.profile = ProfileDto.from(profileImage);
    }
}
