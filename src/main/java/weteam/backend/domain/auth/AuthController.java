package weteam.backend.domain.auth;

import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import weteam.backend.application.auth.jwt.service.FirebaseJwtService;
import weteam.backend.application.swagger.SwaggerOK;

@RestController
@Validated
@RequestMapping("/api/auths")
@RequiredArgsConstructor
@Tag(name = "AUTH")
public class AuthController {
    private final FirebaseJwtService firebaseJwtService;

    @GetMapping("{uid}")
    @SwaggerOK(summary = "jwt 발급 api - 개발용")
    public ResponseEntity<String> getJwt(@PathVariable("uid") final String uid) throws FirebaseAuthException {
        String token = firebaseJwtService.generateFirebaseJwtToken(uid);
        return ResponseEntity.ok(token);
    }
}
