package weteam.backend.application.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.firebase.common.PushMessage;
import weteam.backend.application.handler.exception.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.alarm.Alarm;
import weteam.backend.domain.user.UserRepository;
import weteam.backend.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FirebaseService {
  private final FirebaseMessaging firebaseMessaging;
  private final UserRepository userRepository;
  private final SecurityUtil securityUtil;

  public String readDeviceToken() {
    Optional<User> user = userRepository.findById(securityUtil.getId());
    return user.map(User::getDeviceToken).orElseThrow(() -> new CustomException(CustomErrorCode.BAD_REQUEST, "디바이스 토큰을 조회할 수 없습니다."));
  }

  @Transactional
  public void updateDevice(final String token) {
    User user = userRepository.findById(securityUtil.getId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND, "사용자를 조회할 수 없습니다."));
    user.updateDevice(token);
  }

  @Transactional
  public void sendNotification(final List<Alarm> alarmList) {
    if (alarmList != null && !alarmList.isEmpty()) {
      final List<Message> messageList = PushMessage.makeMessageList(alarmList);
      try {
        firebaseMessaging.sendAll(messageList);
      } catch (FirebaseMessagingException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
