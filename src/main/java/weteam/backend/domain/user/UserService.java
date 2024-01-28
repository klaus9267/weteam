package weteam.backend.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.CustomErrorCode;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.project.repository.ProjectRepository;
import weteam.backend.domain.user.dto.UserWithProfileImageDto;
import weteam.backend.domain.user.entity.User;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final SecurityUtil securityUtil;

    public UserWithProfileImageDto findOneById(final Long userId, final boolean isMine) {
        final User user = this.findOne(userId);
        return UserWithProfileImageDto.from(user, isMine);
    }

    private User findOne(final Long id) {
        return userRepository.findById(id).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND));
    }

    @Transactional
    public void updateOrganization(final String organization) {
        final User user = this.findOne(securityUtil.getId());
        user.updateOrganization(organization);
    }

    @Transactional
    public void delete() {
        final Long userId = securityUtil.getId();
        if (projectRepository.existsByHostId(userId)) {
            throw new CustomException(CustomErrorCode.BAD_REQUEST, "호스트로 진행중인 팀플이 존재합니다.");
        }
        userRepository.deleteById(userId);
    }
}

