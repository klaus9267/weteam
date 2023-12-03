package weteam.backend.config.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class Message<T>{
    @Schema(description = "요청 결과", nullable = false, example = "true")
    private boolean result;

    @Schema(description = "상태 코드", nullable = false, example = "200")
    private HttpStatus httpStatus;

    @Schema(description = "메세지", nullable = false, example = "요청 성공")
    private String message;

    @Schema(description = "데이터")
    private T data ;

    public Message(HttpStatus httpStatus, T data) {
        this.result = true;
        this.httpStatus = httpStatus;
        this.data = data;
    }

    public Message(T data) {
        this.result = true;
        this.httpStatus = HttpStatus.OK;
        this.data = data;
    }

    public Message(String message) {
        this.result = true;
        this.httpStatus = HttpStatus.OK;
        this.message = message;
    }
}
