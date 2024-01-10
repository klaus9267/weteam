package weteam.backend.application;

import lombok.Getter;

@Getter
public enum Message {
    NOT_FOUND(404, "조회할 대상을 찾을 수 없습니다"),
    BAD_REQUEST(400, "잘못된 요청사항입니다."),
    DUPLICATE(400, "이미 존재하는 데이터입니다."),
    WRONG_TOKEN(401, "잘못된 토큰입니다."),
    EXPIRED_TOKEN(401, "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(401, "지원되지 않는 토큰입니다."),
    WRONG_SIGNATURE(401,"잘못된 JWT 서명입니다."),
    ACCESS_DENIED(401,"접근이 거부됬습니다."),
    NOT_FOUND_PROVIDER(404,"프로바이더를 찾을 수 없습니다"),
    INVALID_HEADER(401,"invalid_header"),
    INVALID_TOKEN(401,"invalid_token")
    ;

    private final Integer httpStatus;
    private final String message;

    Message(Integer httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
