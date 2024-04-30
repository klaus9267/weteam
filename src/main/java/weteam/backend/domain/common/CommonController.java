package weteam.backend.domain.common;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import weteam.backend.domain.common.swagger.SwaggerOK;

import java.util.Base64;

@RestController
@Validated
@RequestMapping("/api/common")
@RequiredArgsConstructor
@Tag(name = "COMMON")
public class CommonController {
  @GetMapping("{url}")
  @SwaggerOK(summary = "딥링크용 url 반환 API")
  public ResponseEntity<String> returnURL(@PathVariable("url") final String url) {
    final String decodedUrl = new String(Base64.getDecoder().decode(url));
    final String html = String.format("""
        <html>
            <meta http-equiv="refresh" content="0; url=%s"></meta>
        </html>
        """, decodedUrl);
    return ResponseEntity.ok(html);
  }
}
