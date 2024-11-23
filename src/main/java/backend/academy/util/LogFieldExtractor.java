package backend.academy.util;

import backend.academy.model.LogRecord;

public class LogFieldExtractor {

    public static Object extractField(LogRecord logRecord, String fieldName) {
        return switch (fieldName) {
            case "method" -> logRecord.method();
            case "from", "to" -> logRecord.localDate();
            case "httpCode" -> logRecord.httpStatus().httpCode();
            case "agent" -> logRecord.httpUserAgent();
            case "remote-address" -> logRecord.remoteAddr();
            case "remote-user" -> logRecord.remoteUser();
            case "bytes" -> logRecord.bytes();
            case "request" -> logRecord.request();
            default -> throw new IllegalStateException("Unexpected field: " + fieldName);
        };
    }

}
