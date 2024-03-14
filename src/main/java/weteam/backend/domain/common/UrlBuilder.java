package weteam.backend.domain.common;

import io.netty.handler.codec.http.HttpScheme;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class UrlBuilder {

  public static String createInviteUrl(final String endPoint, final String hashedId) {
    return UriComponentsBuilder.fromPath("/").scheme(HttpScheme.HTTP.toString()).host("15.164.221.170").port(9090).path(endPoint + hashedId).toUriString();
  }
}
