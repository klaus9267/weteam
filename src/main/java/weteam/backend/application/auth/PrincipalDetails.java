package weteam.backend.application.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import weteam.backend.domain.user.dto.UserDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
public class PrincipalDetails implements UserDetails {
  private final UserDto user;
  
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList((GrantedAuthority) () -> user.role().getKey());
  }
  
  @Override
  public String getUsername() {
    return null;
  }
  
  @Override
  public String getPassword() {
    return null;
  }
  
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }
  
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }
  
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }
  
  @Override
  public boolean isEnabled() {
    return true;
  }
}
