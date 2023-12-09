package weteam.backend.config.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@Builder
public class ApiMetaData<T>{
    @Schema(description = "요청 결과")
    private boolean result;

    @Schema(description = "상태 코드")
    private HttpStatus httpStatus;

    @Schema(description = "메세지")
    private String message;

    @Schema(description = "데이터")
    private T data ;

    public ApiMetaData(HttpStatus httpStatus, T data) {
        this.result = true;
        this.httpStatus = httpStatus;
        this.data = data;
    }

    public ApiMetaData(T data) {
        this.result = true;
        this.httpStatus = HttpStatus.OK;
        this.data = data;
    }

    public ApiMetaData(String message) {
        this.result = true;
        this.httpStatus = HttpStatus.OK;
        this.message = message;
    }

    public ApiMetaData(HttpStatus status) {
        this.result = true;
        this.httpStatus = status;
    }
}
