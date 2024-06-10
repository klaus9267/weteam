package weteam.backend.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.junit.jupiter.api.BeforeEach;
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

import static java.lang.System.getenv;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@Import({DataInitializer.class,TestRepository.class})
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class BaseIntegrationTest {
  Map<String, String> env = getenv();
  protected ObjectMapper mapper = new ObjectMapper();

  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  protected TestRepository testRepository;
  protected String idToken;
  protected String uid = env.get("uid");

  public BaseIntegrationTest() {
    this.mapper.registerModule(new JavaTimeModule());
  }

  @BeforeEach
  public void setup(@Autowired FirebaseAuth firebaseAuth) throws FirebaseAuthException, IOException, ParseException {
    UserRecord userRecord = firebaseAuth.getUserByEmail("klaus9267@gmail.com");
    String customToken = firebaseAuth.createCustomToken(userRecord.getUid());
    idToken = "Bearer " + exchangeCustomTokenForIdToken(customToken);
  }

  private String exchangeCustomTokenForIdToken(String customToken) throws IOException, ParseException {
    String apiKey = env.get("apiKey");
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
