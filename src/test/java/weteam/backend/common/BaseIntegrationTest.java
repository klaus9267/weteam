package weteam.backend.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(DataInitializer.class)
@Transactional
public class BaseIntegrationTest {
  protected final ObjectMapper mapper = new ObjectMapper();

  @Autowired
  protected MockMvc mockMvc;
  protected static String idToken;

  @BeforeAll
  public static void setup(@Autowired FirebaseAuth firebaseAuth) throws FirebaseAuthException, IOException, ParseException {
    UserRecord userRecord = firebaseAuth.getUserByEmail("klaus9267@gmail.com");
    String customToken = firebaseAuth.createCustomToken(userRecord.getUid());
    idToken = "Bearer " + exchangeCustomTokenForIdToken(customToken);
  }

  private static String exchangeCustomTokenForIdToken(String customToken) throws IOException, ParseException {
    String apiKey = "AIzaSyBc4I9eLDbOYxWfTtjWD6JrMymeTy7UQm0";
    String requestUrl = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithCustomToken?key=" + apiKey;

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
        return (String) responseMap.get("idToken");
      }
    }
  }
}
