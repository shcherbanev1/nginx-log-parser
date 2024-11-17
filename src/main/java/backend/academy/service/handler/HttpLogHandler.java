package backend.academy.service.handler;

import backend.academy.filter.LogFilter;
import backend.academy.model.LogsStatistic;
import backend.academy.service.LogService;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpLogHandler implements LogHandler {

    @Override
    public boolean fetchLogs(String url, LogService logService, LogsStatistic logsStatistic) {
        return fetchLogsWithFilter(url, logService, logsStatistic, null);
    }

    @Override
    public boolean fetchLogs(String url, LogService logService, LogsStatistic logsStatistic, LogFilter[] filters) {
        return fetchLogsWithFilter(url, logService, logsStatistic, filters);
    }

    @SuppressWarnings("MagicNumber")
    private boolean fetchLogsWithFilter(
        String url, LogService logService,
        LogsStatistic logsStatistic, LogFilter[] filters
    ) {
        boolean result = false;
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
            try {
                HttpResponse<java.io.InputStream> response =
                    httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
                if (response.statusCode() == 200) {
                    result = true;
                    logsStatistic.addSource(url);
                    try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.body(), StandardCharsets.UTF_8))) {
                        reader.lines().forEach(it -> {
                                if (filters != null) {
                                    logService.addLog(logService.parse(it), logsStatistic, filters);
                                } else {
                                    logService.addLog(logService.parse(it), logsStatistic);
                                }
                            }
                        );
                    }
                } else {
                    log.error("HTTP Request failed with status code: {}", response.statusCode());
                }
            } catch (Exception e) {
                log.error("Error fetching logs: {}", e.getMessage());
            }
        }
        return result;
    }
}
