package weteam.backend.application.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
@Builder
public class ExceptionResponse {
    private String field;
    private String message;
}
