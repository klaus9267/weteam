package weteam.backend.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.Message;
import weteam.backend.application.handler.exception.NotFoundException;
import weteam.backend.domain.user.dto.UserDto;
import weteam.backend.domain.user.entity.User;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto findOneById(Long id) {
        User user = this.findOne(id);
        return UserDto.from(user);
    }

    private User findOne(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(Message.NOT_FOUND));
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
        User user = this.findOne(id);
        userRepository.delete(user);
    }
}

