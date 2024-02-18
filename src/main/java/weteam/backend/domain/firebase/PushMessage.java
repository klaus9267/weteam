package weteam.backend.domain.firebase;

import com.google.firebase.messaging.Message;
import weteam.backend.application.handler.exception.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.alarm.Alarm;
import weteam.backend.domain.alarm.AlarmStatus;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.user.entity.User;

import java.util.List;

public record PushMessage(
    String title,
    String content
) {
  public static List<Message> makeMessageList(List<Alarm> alarmList) {
    if (alarmList.isEmpty()) {
      throw new CustomException(CustomErrorCode.BAD_REQUEST, "보낼 알람이 없습니다.");
    }
    if (alarmList.get(0).getTargetUser() == null) {
      return alarmList.stream().map(alarm -> {
        final PushMessage pushMessage = PushMessage.makeMessage(alarm.getProject(), alarm.getStatus());
        return Message.builder()
                      .setToken(alarm.getUser().getDeviceToken())
                      .putData(pushMessage.title, pushMessage.content)
                      .build();
      }).toList();
    } else {
      return alarmList.stream().map(alarm -> {
        final PushMessage pushMessage = PushMessage.makeMessage(alarm.getProject(), alarm.getStatus(), alarm.getTargetUser());
        return Message.builder()
                      .setToken(alarm.getUser().getDeviceToken())
                      .putData(pushMessage.title, pushMessage.content)
                      .build();
      }).toList();
    }
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
      default -> throw new CustomException(CustomErrorCode.BAD_REQUEST, "잘못된 요청입니다");
    };
  }
}
