package weteam.backend.domain.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.handler.exception.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.alarm.Alarm;
import weteam.backend.domain.user.UserRepository;
import weteam.backend.domain.user.entity.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FirebaseService {
  private final FirebaseMessaging firebaseMessaging;
  private final UserRepository userRepository;
  private final SecurityUtil securityUtil;
  
  @Transactional
  public void updateDevice(final String token) {
    User user = userRepository.findById(securityUtil.getId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND, "사용자를 조회할 수 없습니다."));
    user.updateDevice(token);
  }
  
  @Transactional
  public void sendNotification(final List<Alarm> alarmList) {
    final List<Message> messageList = PushMessage.makeMessageList(alarmList);
    try {
      firebaseMessaging.sendAll(messageList);
    } catch (FirebaseMessagingException e) {
      throw new CustomException(CustomErrorCode.FIREBASE_MESSAGE_ERROR, e.getMessage());
    }
  }
}
