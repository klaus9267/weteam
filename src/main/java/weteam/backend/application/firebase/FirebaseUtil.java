package weteam.backend.application.firebase;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.application.handler.exception.ErrorCode;

import java.util.HashMap;
import java.util.Map;

@Component
@Setter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "firebase")
public class FirebaseUtil {
  private final FirebaseAuth firebaseAuth;
  private final ObjectMapper objectMapper = new ObjectMapper();

  private String key;
  private String email;

  public String createIdToken() {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      String requestUrl = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithCustomToken?key=" + key;
      UserRecord userRecord = firebaseAuth.getUserByEmail(email);
      String customToken = firebaseAuth.createCustomToken(userRecord.getUid());


      HttpPost httpPost = new HttpPost(requestUrl);
      Map<String, String> requestBody = new HashMap<>();
      requestBody.put("token", customToken);
      requestBody.put("returnSecureToken", "true");

      StringEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(requestBody));
      httpPost.setEntity(entity);
      httpPost.setHeader("Content-Type", "application/json");

      CloseableHttpResponse response = httpClient.execute(httpPost);
      String responseBody = EntityUtils.toString(response.getEntity());
      JsonNode responseNode = objectMapper.readTree(responseBody);
      return responseNode.get("idToken").asText();
    } catch (Exception e) {
      throw new CustomException(ErrorCode.WRONG_TOKEN);
    }
  }
}
