package backend.academy.filter;

import backend.academy.model.LogRecord;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogFilter implements Predicate<LogRecord> {

    private final String fieldName;
    private final String value;
    private final String operator;

    public LogFilter(String fieldName, String value, String operator) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    @Override
    public boolean test(LogRecord logRecord) {
        Object fieldValue = getFieldValue(logRecord, fieldName);
        if (fieldValue == null) {
            return false;
        }

        try {
            if ("=".equals(operator)) {
                return fieldValue.toString().equals(value);
            } else if (fieldValue instanceof LocalDateTime dateValue) {
                LocalDate filterDate = LocalDate.parse(value);
                return switch (operator) {
                    case ">=" -> dateValue.isAfter(filterDate.atStartOfDay()) || dateValue.isEqual(
                        filterDate.atStartOfDay());
                    case "<=" -> dateValue.isBefore(filterDate.atStartOfDay()) || dateValue.isEqual(
                        filterDate.atStartOfDay());
                    default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
                };
            }
        } catch (IllegalArgumentException e) {
            log.error("An error occurred in filter - {}", e.getMessage());
        }
        return false;
    }

    private Object getFieldValue(LogRecord logRecord, String fieldName) {
        return switch (fieldName) {
            case "method" -> logRecord.method();
            case "from", "to" -> logRecord.localDate();
            case "httpCode" -> logRecord.httpStatus().httpCode();
            case "agent" -> logRecord.httpUserAgent();
            case "remote-address" -> logRecord.remoteAddr();
            case "remote-user" -> logRecord.remoteUser();
            case "bytes" -> logRecord.bytes();
            case "request" -> logRecord.request();
            default -> throw new IllegalStateException("Unexpected value: " + fieldName);
        };
    }

}
