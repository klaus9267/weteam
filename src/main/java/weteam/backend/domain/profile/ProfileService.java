package weteam.backend.domain.profile;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.CustomErrorCode;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.profile.dto.ProfileDto;
import weteam.backend.domain.user.UserRepository;
import weteam.backend.domain.user.entity.User;

@Service
@AllArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final SecurityUtil securityUtil;

    @Transactional
    public void addProfileImage(final Long imageIdx) {
        User user = userRepository.findById(securityUtil.getId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND));
        final ProfileImage image = ProfileImage.from(imageIdx, user);
        profileRepository.save(image);
    }

    @Transactional
    public void updateProfileImage(final Long imageIdx) {
        ProfileImage image = this.findOne();
        image.updateImage(imageIdx);
    }

    private ProfileImage findOne() {
        return profileRepository.findByUserId(securityUtil.getId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND));
    }
}
