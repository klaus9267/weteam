package weteam.backend.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.project.repository.ProjectRepository;
import weteam.backend.domain.user.dto.UserDto;
import weteam.backend.domain.user.entity.User;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public UserDto findOneById(Long id) {
        User user = this.findOne(id);
        return UserDto.from(user);
    }

    private User findOne(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND));
    }

    @Transactional
    public void updateOrganization(Long id, String organization) {
        User user = this.findOne(id);
        user.updateOrganization(organization);
    }

    @Transactional
    public void updateInfo(Long userId) {
        User user = this.findOne(userId);
    }

    @Transactional
    public void delete(Long id) {
        if (projectRepository.existsByHostId(id)) {
            throw new CustomException(CustomErrorCode.BAD_REQUEST, "호스트로 진행중인 팀플이 존재합니다.");
        }
        userRepository.deleteById(id);
    }
}

