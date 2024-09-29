package weteam.backend.application.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.firebase.common.PushMessage;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.application.handler.exception.ErrorCode;
import weteam.backend.domain.alarm.entity.Alarm;
import weteam.backend.domain.user.UserRepository;
import weteam.backend.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseService {
  private final FirebaseMessaging firebaseMessaging;
  private final UserRepository userRepository;
  private final SecurityUtil securityUtil;

  public String readDeviceToken() {
    Optional<User> user = userRepository.findById(securityUtil.getCurrentUserId());
    return user.map(User::getDeviceToken).orElseThrow(ErrorCode.DEVICE_TOKEN_NOT_FOUND);
  }

  @Transactional
  public void updateDevice(final String token) {
    User user = userRepository.findById(securityUtil.getCurrentUserId()).orElseThrow(ErrorCode.USER_NOT_FOUND);
    user.updateDevice(token);
  }

  @Transactional
  public void sendNotification(final List<Alarm> alarmList) {
    if (alarmList != null && !alarmList.isEmpty()) {
      final List<Message> messageList = PushMessage.makeMessageList(alarmList);
      if (!messageList.isEmpty()) {
        try {
          firebaseMessaging.sendAll(messageList);
        } catch (FirebaseMessagingException e) {
          throw new CustomException(ErrorCode.FIREBASE_MESSAGE_ERROR);
        }
      }
    }
  }
}
