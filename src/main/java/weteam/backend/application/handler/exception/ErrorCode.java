package weteam.backend.application.handler.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public enum ErrorCode implements Supplier<CustomException> {
  NOT_FOUND(HttpStatus.NOT_FOUND, "조회할 대상을 찾을 수 없습니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "조회할 사용자를 찾을 수 없습니다."),
  PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "조회할 프로젝트를 찾을 수 없습니다."),
  PROJECT_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "조회할 프로젝트 멤버를 찾을 수 없습니다."),
  MEETING_NOT_FOUND(HttpStatus.NOT_FOUND, "조회할 약속을 찾을 수 없습니다."),
  DEVICE_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "디바이스 토큰을 조회할 수 없습니다."),

  DUPLICATE(HttpStatus.CONFLICT, "이미 존재하는 데이터입니다."),
  MEETING_TIME_DUPLICATE(HttpStatus.CONFLICT, "겹치는 시간대가 있습니다"),

  WRONG_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),
  EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
  UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 토큰입니다."),
  WRONG_SIGNATURE(HttpStatus.UNAUTHORIZED, "잘못된 JWT 서명입니다."),

  ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근이 거부됬습니다."),

  BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청사항입니다."),
  INVALID_TIME(HttpStatus.BAD_REQUEST, "시작, 종료일을 확인해주세요."),
  PROJECT_ACCESS_DENIED(HttpStatus.BAD_REQUEST, "해당 방에 입장할 수 없습니다."),
  ALREADY_ACCESS_MEETING(HttpStatus.BAD_REQUEST, "이미 수락한 약속입니다."),
  ALREADY_ACCESS_PROJECT(HttpStatus.BAD_REQUEST, "이미 수락한 프로젝트입니다."),
  ALREADY_EXIT_PROJECT(HttpStatus.BAD_REQUEST, "이미 탈퇴한 프로젝트입니다."),
  USER_IS_HOST(HttpStatus.BAD_REQUEST, "호스트로 진행중인 팀플이 존재합니다."),
  INVALID_DATA(HttpStatus.BAD_REQUEST, "잘못된 데이터입니다."),
  INVALID_TYPE(HttpStatus.BAD_REQUEST, "잘못된 알람 타입입니다."),
  INVALID_USER(HttpStatus.BAD_REQUEST, "잘못된 사용자의 접근입니다."),
  INVALID_HOST(HttpStatus.BAD_REQUEST, "호스트가 아닙니다."),
  INVALID_LIST(HttpStatus.BAD_REQUEST, "배열에 데이터가 없습니다."),
  NOT_EXIST_UNREAD_ALARM(HttpStatus.BAD_REQUEST, "미확인 알람이 없습니다."),
  IO_EXCEPTION(HttpStatus.BAD_REQUEST, "IOException!!"),
  NULL(HttpStatus.BAD_REQUEST, "Required value is null"),
  ILLEGAL_ACCESS(HttpStatus.BAD_REQUEST, "잘못된 필드의 접근"),

  FIREBASE_MESSAGE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send notification");

  private final HttpStatus httpStatus;
  private final String message;

  @Override
  public CustomException get() {
    return new CustomException(this);
  }
}
