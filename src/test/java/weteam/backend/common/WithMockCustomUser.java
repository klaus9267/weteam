package weteam.backend.common;

import org.springframework.security.test.context.support.WithSecurityContext;
import weteam.backend.domain.user.entity.UserRole;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
  String uid() default "uid is required";

  UserRole role() default UserRole.USER;
}
