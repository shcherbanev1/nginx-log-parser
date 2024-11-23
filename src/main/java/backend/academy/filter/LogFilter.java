package backend.academy.filter;

import backend.academy.model.LogRecord;
import java.util.function.Predicate;

public abstract class LogFilter implements Predicate<LogRecord> {

    protected final String fieldName;

    protected LogFilter(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public abstract boolean test(LogRecord logRecord);

}
