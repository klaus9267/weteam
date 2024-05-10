package weteam.backend.application.firebase;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import weteam.backend.application.handler.exception.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.alarm.Alarm;
import weteam.backend.domain.alarm.AlarmStatus;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.user.entity.User;

import java.util.Calendar;
import java.util.List;

public record PushMessage(
    String title,
    String content
) {
  public static List<Message> makeMessageList(List<Alarm> alarmList) {
    final Alarm firstAlarm = alarmList.get(0);
    final PushMessage pushMessage = firstAlarm.getTargetUser() == null
        ? PushMessage.makeMessage(firstAlarm.getProject(), firstAlarm.getStatus())
        : PushMessage.makeMessage(firstAlarm.getProject(), firstAlarm.getStatus(), firstAlarm.getTargetUser());

    final Notification notification = Notification.builder()
        .setTitle(pushMessage.title)
        .setBody(pushMessage.content)
        .build();

    return alarmList.stream().map(alarm -> Message.builder()
        .setToken(alarm.getUser().getDeviceToken())
        .setNotification(notification)
        .build()
    ).toList();
  }

  public static PushMessage makeMessage(final Project project, final AlarmStatus status, final User targetUser) {
    return switch (status) {
      case JOIN -> {
        final String title = project.getName() + "에 " + targetUser.getUsername() + "님이 참가했습니다!";
        final String content = "모두 새로운 멤버를 환영해주세요!\uD83D\uDC4B";
        yield new PushMessage(title, content);
      }
      case EXIT -> {
        final String title = project.getName() + "에 " + targetUser.getUsername() + "님이 퇴장했습니다.";
        final String content = "이 멤버와는 더 이상 함께하실수 없습니다...\uD83D\uDE22";
        yield new PushMessage(title, content);
      }
      case CHANGE_HOST -> {
        final String title = project.getName() + "의 호스트가 " + targetUser.getUsername() + "님으로 변경되었습니다.";
        final String content = "새로운 호스트를 격하게 응원해주세요\uD83D\uDC83";
        yield new PushMessage(title, content);
      }
      case KICK -> {
        final String title = project.getName() + "의 " + targetUser.getUsername() + "님이 퇴출되었어요.";
        final String content = "안녕히가세요\uD83D\uDE22";
        yield new PushMessage(title, content);
      }
      default -> throw new CustomException(CustomErrorCode.BAD_REQUEST, "잘못된 요청입니다");
    };
  }

  public static PushMessage makeMessage(final Project project, final AlarmStatus status) {
    return switch (status) {
      case UPDATE_PROJECT -> {
        final String title = project.getName() + "의 정보가 변경되었습니다! 확인해주세요!";
        final String content = "새로운 마음으로 다시 시작하는 팀플❤";
        yield new PushMessage(title, content);
      }
      case DONE -> {
        final String title = project.getName() + "가 완료되었습니다.";
        final String content = "그동안 수고하셨습니다 :)";
        yield new PushMessage(title, content);
      }
      case NEW_MEETING -> {
        final String title = project.getName() + "에서 새로운 언제보까가 생성되었습니다! 확인해주세요!";
        final String content = "우리 언제 만날까...?";
        yield new PushMessage(title, content);
      }
      default -> throw new CustomException(CustomErrorCode.BAD_REQUEST, "잘못된 요청입니다");
    };
  }
}
