package weteam.backend.application.auth.jwt.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FirebaseJwtService {
    private final FirebaseAuth firebaseAuth;

    public String generateFirebaseJwtToken(String uid) throws FirebaseAuthException {
        return firebaseAuth.createCustomToken(uid);
    }
}
