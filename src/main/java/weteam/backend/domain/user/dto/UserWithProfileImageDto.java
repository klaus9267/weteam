package weteam.backend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
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

    private UserWithProfileImageDto(final User user, final boolean isMine) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = isMine ? user.getEmail() : null;
        this.organization = user.getOrganization();
        this.role = user.getRole();
        this.profile = user.getProfileImage() == null ? null : ProfileDto.from(user.getProfileImage());
    }

    public static UserWithProfileImageDto from(final User user, final boolean isMine) {
        return new UserWithProfileImageDto(user, isMine);
    }
}
