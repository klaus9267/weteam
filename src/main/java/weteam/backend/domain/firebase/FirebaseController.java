package weteam.backend.domain.firebase;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import weteam.backend.domain.common.swagger.SwaggerNoContent;
import weteam.backend.domain.common.swagger.SwaggerOK;

@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
@Tag(name = "FCM")
public class FirebaseController {
  private final FirebaseService firebaseService;
  
  @PatchMapping("{token}")
  @SwaggerNoContent(summary = "디바이스 설정 및 변경")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateDevice(@PathVariable("token") final String token) {
    firebaseService.updateDevice(token);
  }
}

