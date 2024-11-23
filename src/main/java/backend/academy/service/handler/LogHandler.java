package backend.academy.service.handler;

import backend.academy.filter.LogFilter;
import backend.academy.model.LogsStatistic;
import backend.academy.service.LogService;
import java.util.List;

public interface LogHandler {

    boolean fetchLogs(String url, LogService logService, LogsStatistic logsStatistic);

    boolean fetchLogs(String url, LogService logService, LogsStatistic logsStatistic, List<LogFilter> filters);
}
