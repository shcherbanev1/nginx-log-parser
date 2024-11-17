package backend.academy.service.handler;

import backend.academy.filter.LogFilter;
import backend.academy.model.LogsStatistic;
import backend.academy.service.LogService;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileLogHandler implements LogHandler {

    @Override
    public boolean fetchLogs(String filePath, LogService logService, LogsStatistic logsStatistic) {
        return fetchLogsWithFilter(filePath, logService, logsStatistic, null);
    }

    @Override
    public boolean fetchLogs(String filePath, LogService logService, LogsStatistic logsStatistic, LogFilter[] filters) {
        FilePathHandler filePathHandler = new FilePathHandler();
        try {
            filePathHandler.getMatchingFiles(filePath)
                .forEach(it -> {
                    fetchLogsWithFilter(it.toString(), logService, logsStatistic, filters);
                    logsStatistic.addSource(it.toString());
                });
            return true;
        } catch (IOException e) {
            log.error("Invalid file name {}", filePath);
            return false;
        }
    }

    private boolean fetchLogsWithFilter(
        String filePath,
        LogService logService,
        LogsStatistic logsStatistic,
        LogFilter[] filters
    ) {
        boolean result = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (filters != null) {
                    logService.addLog(logService.parse(line), logsStatistic, filters);
                } else {
                    logService.addLog(logService.parse(line), logsStatistic);
                }
            }
            result = true;
        } catch (IOException e) {
            log.error("Error reading file: {}", e.getMessage());
        }
        return result;
    }
}
