package backend.academy.util;

import backend.academy.filter.LogFilter;
import backend.academy.service.handler.FileLogHandler;
import backend.academy.service.handler.HttpLogHandler;
import backend.academy.service.handler.LogHandler;
import backend.academy.service.writer.ReportWriter;
import backend.academy.type.ReportFormat;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class InstanceFabric {

    public static LogHandler createLogHandler(String path) {
        if (isValidURL(path)) {
            return new HttpLogHandler();
        }
        return new FileLogHandler();
    }

    public static LogFilter[] createFilters(
        List<String> filterFields,
        List<String> filterValues,
        String from,
        String to
    ) {
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
        return ReportFormat.fromString(format).reportWriter();
    }

    private boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

}
