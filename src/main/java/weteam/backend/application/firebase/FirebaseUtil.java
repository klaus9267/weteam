package weteam.backend.application.firebase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FirebaseUtil {
  private final FirebaseAuth firebaseAuth;

  @Value("${firebase.key}")
  private String apiKey;

  @Value("${firebase.email}")
  private String email;

  public String createIdToken() throws FirebaseAuthException {
    String requestUrl = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithCustomToken?key=" + apiKey;

    UserRecord userRecord = firebaseAuth.getUserByEmail(email);
    String customToken = firebaseAuth.createCustomToken(userRecord.getUid());

    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpPost httpPost = new HttpPost(requestUrl);
      Map<String, String> requestBody = new HashMap<>();
      requestBody.put("token", customToken);
      requestBody.put("returnSecureToken", "true");

      StringEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(requestBody));
      httpPost.setEntity(entity);
      httpPost.setHeader("Content-Type", "application/json");

      CloseableHttpResponse response = httpClient.execute(httpPost);
      String responseBody = EntityUtils.toString(response.getEntity());
      Map<String, Object> responseMap = new ObjectMapper().readValue(responseBody, HashMap.class);

      return (String) responseMap.get("idToken");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
