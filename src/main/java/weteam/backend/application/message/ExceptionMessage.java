package weteam.backend.application.message;

import lombok.Getter;

@Getter
public enum ExceptionMessage {
    NOT_FOUND(404, "조회할 대상을 찾을 수 없습니다"),
    BAD_REQUEST(400, "잘못된 요청사항입니다."),
    DUPLICATE(400, "이미 존재하는 데이터입니다."),
    WRONG_TOKEN(401, "잘못된 토큰입니다."),
    EXPIRED_TOKEN(401, "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(401, "지원되지 않는 토큰입니다."),
    WRONG_SIGNATURE(401,"잘못된 JWT 서명입니다."),
    ACCESS_DENIED(401,"접근이 거부됬습니다.")
    ;

    private final Integer httpStatus;
    private final String message;

    ExceptionMessage(Integer httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
