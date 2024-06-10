package weteam.backend.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import weteam.backend.domain.common.swagger.SwaggerOK;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@RestController
@Validated
@RequestMapping("/api/common")
@RequiredArgsConstructor
@Tag(name = "COMMON")
public class CommonController {
  private final FirebaseAuth firebaseAuth;

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
    String apiKey = "AIzaSyBc4I9eLDbOYxWfTtjWD6JrMymeTy7UQm0";
    String requestUrl = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithCustomToken?key=" + apiKey;

    UserRecord userRecord = firebaseAuth.getUserByEmail("klaus9267@gmail.com");
    String customToken = firebaseAuth.createCustomToken(userRecord.getUid());

    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpPost httpPost = new HttpPost(requestUrl);
      Map<String, String> requestBody = new HashMap<>();
      requestBody.put("token", customToken);
      requestBody.put("returnSecureToken", "true");

      StringEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(requestBody));
      httpPost.setEntity(entity);
      httpPost.setHeader("Content-Type", "application/json");

      try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
        String responseBody = EntityUtils.toString(response.getEntity());
        Map<String, Object> responseMap = new ObjectMapper().readValue(responseBody, HashMap.class);
        String idToken = (String) responseMap.get("idToken");

        return ResponseEntity.ok(idToken);
      } catch (ParseException e) {
        throw new RuntimeException(e);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
