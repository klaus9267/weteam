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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {
  private final String databaseUrl;
  
  public FirebaseConfig(@Value("${firebase.database-url}") final String databaseUrl) {
    this.databaseUrl = databaseUrl;
  }
  
  @Bean
  public FirebaseAuth getFirebaseAuth() throws IOException {
    FileInputStream serviceAccount = new FileInputStream("./firebase.json");
    
    FirebaseOptions options = new FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .setDatabaseUrl(databaseUrl)
        .build();
    
    FirebaseApp.initializeApp(options);
    
    return FirebaseAuth.getInstance();
  }
  
  @Bean
  public FirebaseMessaging firebaseMessaging() throws IOException {
    ClassPathResource resource = new ClassPathResource("./firebase.json");
    InputStream refreshToken = resource.getInputStream();
    FirebaseApp firebaseApp = FirebaseApp.getApps()
                                         .stream()
                                         .filter(app -> FirebaseApp.DEFAULT_APP_NAME.equals(app.getName()))
                                         .findFirst()
                                         .orElseGet(() -> {
                                           try {
                                             final FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(refreshToken)).build();
                                             return FirebaseApp.initializeApp(options);
                                           } catch (IOException e) {
                                             throw new RuntimeException(e);
                                           }
                                         });
    return FirebaseMessaging.getInstance(firebaseApp);
  }
}
