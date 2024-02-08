package weteam.backend.application.auth;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseInitializer {
  private final String databaseUrl;
  
  public FirebaseInitializer(@Value("${firebase.database-url}") final String databaseUrl) {
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
}
