package backend.academy.service.handler;

import backend.academy.filter.LogFilter;
import backend.academy.model.LogsStatistic;
import backend.academy.service.LogService;

public interface LogHandler {

    boolean fetchLogs(String url, LogService logService, LogsStatistic logsStatistic);

    boolean fetchLogs(String url, LogService logService, LogsStatistic logsStatistic, LogFilter[] filters);

}
