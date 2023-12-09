package weteam.backend.config.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
@Builder
public class ErrorResponse {
    private String field;
    private HttpStatus httpStatus;
    private String defaultMessage;
}
