package weteam.backend.domain;

import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import weteam.backend.application.firebase.FirebaseUtil;
import weteam.backend.domain.common.swagger.SwaggerOK;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;


@RestController
@Validated
@RequestMapping("/api/common")
@RequiredArgsConstructor
@Tag(name = "COMMON")
@Slf4j
public class CommonController {
  private final FirebaseUtil firebaseUtil;

  @GetMapping
  @SwaggerOK(summary = "딥링크용 url 반환 API")
  public ResponseEntity<String> returnURL(@RequestParam("url") final String url) {
    final String decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8);
    final String html = String.format("""
        <html>
            <meta http-equiv="refresh" content="0; url=%s"></meta>
        </html>
        """, decodedUrl);
    return ResponseEntity.ok(html);
  }

  @GetMapping("develop")
  @SwaggerOK(summary = "토큰 발급(백엔드용)")
  public ResponseEntity<String> createToken() throws FirebaseAuthException {
    String token = firebaseUtil.createIdToken();
    return ResponseEntity.ok(token);
  }
}
