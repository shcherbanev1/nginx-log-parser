package backend.academy.model;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

@SuppressWarnings("RecordComponentNumber")
public record LogRecord(
    String remoteAddr,
    String remoteUser,
    LocalDateTime localDate,
    String method,
    String request,
    HttpStatus httpStatus,
    int bytes,
    String httpReferer,
    String httpUserAgent
) {
}
