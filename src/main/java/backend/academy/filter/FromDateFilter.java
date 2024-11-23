package backend.academy.filter;

import backend.academy.model.LogRecord;
import backend.academy.util.LogFieldExtractor;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FromDateFilter extends LogFilter {

    private final LocalDate date;

    public FromDateFilter(LocalDate date) {
        super("from");
        this.date = date;
    }

    @Override
    public boolean test(LogRecord logRecord) {
        Object fieldValue = LogFieldExtractor.extractField(logRecord, fieldName);
        if (fieldValue instanceof LocalDateTime dateValue) {
            return dateValue.isAfter(date.atStartOfDay()) || dateValue.isEqual(date.atStartOfDay());
        }
        return false;
    }
}
