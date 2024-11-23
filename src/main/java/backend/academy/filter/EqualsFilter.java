package backend.academy.filter;

import backend.academy.model.LogRecord;
import backend.academy.util.LogFieldExtractor;

public class EqualsFilter extends LogFilter {

    private final String value;

    public EqualsFilter(String fieldName, String value) {
        super(fieldName);
        this.value = value;
    }

    @Override
    public boolean test(LogRecord logRecord) {
        Object fieldValue = LogFieldExtractor.extractField(logRecord, fieldName);
        return fieldValue != null && fieldValue.toString().equals(value);
    }

}
