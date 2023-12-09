package weteam.backend.config.message;

import lombok.Getter;

@Getter
public enum ExceptionMessage {
    NOT_FOUND("조회할 대상을 찾을 수 없습니다") ,
    BAD_REQUEST("잘못된 요청사항입니다.") ,
    DUPLICATE("이미 존재하는 데이터입니다.");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }
}
