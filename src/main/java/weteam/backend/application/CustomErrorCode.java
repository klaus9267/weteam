package weteam.backend.application;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CustomErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "조회할 대상을 찾을 수 없습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "조회할 사용자를 찾을 수 없습니다."),
    NOT_FOUND_PROJECT(HttpStatus.NOT_FOUND, "조회할 프로젝트를 찾을 수 없습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청사항입니다."),
    DUPLICATE(HttpStatus.CONFLICT, "이미 존재하는 데이터입니다."),
    WRONG_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 토큰입니다."),
    WRONG_SIGNATURE(HttpStatus.UNAUTHORIZED, "잘못된 JWT 서명입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근이 거부됬습니다."),
    NOT_FOUND_PROVIDER(HttpStatus.NOT_FOUND, "프로바이더를 찾을 수 없습니다"),
    INVALID_HEADER(HttpStatus.UNAUTHORIZED, "invalid_header"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "invalid_token"),
    INVALID_USER(HttpStatus.BAD_REQUEST, "잘못된 사용자의 접근입니다."),
    INVALID_HOST(HttpStatus.BAD_REQUEST, "호스트가 아닙니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    CustomErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
