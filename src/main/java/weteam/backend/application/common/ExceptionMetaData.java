package weteam.backend.application.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
public class ExceptionMetaData<T> {
    private HttpStatus httpStatus;
    private String message;
    private T data;
}
