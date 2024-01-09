package weteam.backend.application.auth;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Configuration
public class FirebaseInitializer {
    @Value("${firebase.type}")
    private String type;

    @Value("${firebase.project_id}")
    private String projectId;

    @Value("${firebase.private_key_id}")
    private String privateKeyId;

    @Value("${firebase.private_key}")
    private String privateKey;

    @Value("${firebase.client_email}")
    private String clientEmail;

    @Value("${firebase.client_id}")
    private String clientId;

    @Value("${firebase.auth_uri}")
    private String authUri;

    @Value("${firebase.token_uri}")
    private String tokenUri;

    @Value("${firebase.auth_provider_x509_cert_url}")
    private String authProviderX509CertUrl;

    @Value("${firebase.client_x509_cert_url}")
    private String clientX509CertUrl;

    @Value("${firebase.universe_domain}")
    private String universe_domain;

    @Bean
    public FirebaseAuth getFirebaseAuth() throws IOException {
        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(
                new ByteArrayInputStream(createFirebaseConfigString().getBytes(StandardCharsets.UTF_8))
        );

        FirebaseOptions options = FirebaseOptions.builder()
                                                 .setCredentials(credentials)
                                                 .setProjectId(projectId)
                                                 .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }

        return FirebaseAuth.getInstance();
    }

    private String createFirebaseConfigString() {
        String realPrivateKey = privateKey.replace("\\n", "\n");
        String realProjectId = projectId.replace("\\n", "\n");
        String realPrivateKeyId = privateKeyId.replace("\\n", "\n");
        String realClientEmail = clientEmail.replace("\\n", "\n");
        String realClientId = clientId.replace("\\n", "\n");
        String realClientX509CertUrl = clientX509CertUrl.replace("\\n", "\n");
        String realUniverse_domain = universe_domain.replace("\\n", "\n");

        return "{"
               + "\"type\": \"service_account\","
               + "\"project_id\": \"" + realProjectId + "\","
               + "\"private_key_id\": \"" + realPrivateKeyId +"\","
               + "\"private_key\": \"" + realPrivateKey + "\","
               + "\"client_email\": \"" + realClientEmail + "\","
               + "\"client_id\": \"" + realClientId + "\","
               + "\"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\","
               + "\"token_uri\": \"https://oauth2.googleapis.com/token\","
               + "\"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\","
               + "\"client_x509_cert_url\": \"" + realClientX509CertUrl + "\","
               + "\"universe_domain\": \"" + realUniverse_domain + "\""
               + "}";
    }
}
