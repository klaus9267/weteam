package weteam.backend.domain.common;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import weteam.backend.domain.common.swagger.SwaggerOK;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;


@RestController
@Validated
@RequestMapping("/api/common")
@RequiredArgsConstructor
@Tag(name = "COMMON")
public class CommonController {
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
}
