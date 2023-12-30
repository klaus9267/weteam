package weteam.backend.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.Message;
import weteam.backend.application.handler.exception.NotFoundException;
import weteam.backend.domain.user.entity.User;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findOneById(final Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(Message.NOT_FOUND));
    }

    @Transactional
    public void updateOrganization(final Long id, final String organization) {
        User user = this.findOneById(id);
        user.updateOrganization(organization);
    }

    @Transactional
    public void delete(final Long id) {
        User user = this.findOneById(id);
        userRepository.delete(user);
    }
}

