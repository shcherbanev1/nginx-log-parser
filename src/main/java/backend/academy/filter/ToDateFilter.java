package backend.academy.filter;

import backend.academy.model.LogRecord;
import backend.academy.util.LogFieldExtractor;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ToDateFilter extends LogFilter {

    private final LocalDate date;

    public ToDateFilter(LocalDate date) {
        super("to");
        this.date = date;
    }

    @Override
    public boolean test(LogRecord logRecord) {
        Object fieldValue = LogFieldExtractor.extractField(logRecord, fieldName);
        if (fieldValue instanceof LocalDateTime dateValue) {
            return dateValue.isBefore(date.atStartOfDay()) || dateValue.isEqual(date.atStartOfDay());
        }
        return false;
    }

}
