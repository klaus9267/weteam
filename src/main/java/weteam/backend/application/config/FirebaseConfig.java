package weteam.backend.application.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {
  private final String databaseUrl;
  private final ClassPathResource resource;

  private static FirebaseApp firebaseApp;

  public FirebaseConfig(@Value("${firebase.database-url}") final String databaseUrl) {
    this.databaseUrl = databaseUrl;
    this.resource = new ClassPathResource("./firebase.json");
  }

  @Bean
  public FirebaseAuth getFirebaseAuth() throws IOException {
    if (firebaseApp == null) {
      initializeFirebaseApp();
    }
    return FirebaseAuth.getInstance(firebaseApp);
  }

  @Bean
  public FirebaseMessaging firebaseMessaging() throws IOException {
    if (firebaseApp == null) {
      initializeFirebaseApp();
    }
    return FirebaseMessaging.getInstance(firebaseApp);
  }

  private synchronized void initializeFirebaseApp() throws IOException {
    if (firebaseApp == null || FirebaseApp.getApps().isEmpty()) {
      InputStream serviceAccount = resource.getInputStream();
      FirebaseOptions options = FirebaseOptions.builder()
          .setCredentials(GoogleCredentials.fromStream(serviceAccount))
          .setDatabaseUrl(databaseUrl)
          .build();

      firebaseApp = FirebaseApp.initializeApp(options);
    } else {
      firebaseApp = FirebaseApp.getInstance();
    }
  }
}
