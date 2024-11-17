package backend.academy.util;

import backend.academy.filter.LogFilter;
import backend.academy.service.handler.FileLogHandler;
import backend.academy.service.handler.HttpLogHandler;
import backend.academy.service.handler.LogHandler;
import backend.academy.service.writer.AdocWriter;
import backend.academy.service.writer.MarkDownWriter;
import backend.academy.service.writer.ReportWriter;
import lombok.experimental.UtilityClass;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class InstanceFabric {

    private final static String ADOC_FORMAT = "adoc";
    private final static String MARKDOWN_FORMAT = "md";

    public static LogHandler createLogHandler(String path) {
        if (path.contains("http")) {
            return new HttpLogHandler();
        }
        return new FileLogHandler();
    }

    public static LogFilter[] createFilters(List<String> filterFields, List<String> filterValues, String from, String to) {
        List<LogFilter> filters = new ArrayList<>();
        if (filterFields != null && filterValues != null) {
            for (int i = 0; i < filterFields.size(); i++) {
                filters.add(new LogFilter(filterFields.get(i), filterValues.get(i), "="));
            }
        }
        if (from != null) {
            filters.add(new LogFilter("from", from, ">="));
        }
        if (to != null) {
            filters.add(new LogFilter("to", to, "<="));
        }
        return filters.toArray(new LogFilter[0]);
    }

    public static ReportWriter createReportWriter(String format) {
        if (ADOC_FORMAT.equals(format)) {
            return new AdocWriter();
        }
        return new MarkDownWriter();
    }

    public static String generateReportFilePath(String filename, String format) {
        return System.getProperty("user.dir") + filename + "."
            + (format == null ? MARKDOWN_FORMAT
            : ADOC_FORMAT.equals(format) ? ADOC_FORMAT : MARKDOWN_FORMAT);
    }

}
