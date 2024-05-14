package weteam.backend.common;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import weteam.backend.domain.user.UserRepository;
import weteam.backend.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@TestConfiguration
@RequiredArgsConstructor
public class DataInitializer {
  private final UserRepository userRepository;
  List<User> users = new ArrayList<>();

  @PostConstruct
  public void setContext() {
    this.saveUsers();
  }

  private void saveUsers() {
    List<User> userList = new ArrayList<>();
    userList.add(User.builder().username("kim").uid("hIGOWUmXSugwCftVJ2HsF9kiqfh1").build());
    for (int i = 0; i < 100; i++) {
      User user = User.builder()
          .username("username" + i)
          .uid("uid" + i)
          .build();
      userList.add(user);
    }
    users = userRepository.saveAll(userList);
  }
}
