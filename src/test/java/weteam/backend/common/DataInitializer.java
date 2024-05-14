package weteam.backend.common;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import weteam.backend.domain.user.UserRepository;
import weteam.backend.domain.user.entity.User;
import weteam.backend.domain.user.entity.UserRole;

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
    final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(users.get(0), null, List.of(new SimpleGrantedAuthority(UserRole.USER.getKey())));
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private void saveUsers() {
    List<User> userList = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
//      User user = new User(null, "user" + i, "password", "name" + i, "nickname" + i, "email" + i + "@email.com", UserRole.USER, null, null, null, null);
//      userList.add(user);
    }
    users = userRepository.saveAll(userList);
  }
}
