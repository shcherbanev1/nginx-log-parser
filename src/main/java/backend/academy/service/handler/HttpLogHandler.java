package backend.academy.service.handler;

import backend.academy.filter.LogFilter;
import backend.academy.model.LogsStatistic;
import backend.academy.service.LogService;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpLogHandler implements LogHandler {

    @Override
    public boolean fetchLogs(String url, LogService logService, LogsStatistic logsStatistic) {
        return fetchLogs(url, logService, logsStatistic, null);
    }

    @SuppressWarnings("MagicNumber")
    @Override
    public boolean fetchLogs(String url, LogService logService, LogsStatistic logsStatistic, List<LogFilter> filters) {
        boolean result = false;
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<java.io.InputStream> response = sendHttpRequest(url, httpClient);

        if (response != null && response.statusCode() == 200) {
            result = true;
            logsStatistic.addSource(url);
            processLogLines(response, logService, logsStatistic, filters);
        } else if (response != null) {
            log.error("HTTP Request failed with status code: {}", response.statusCode());
        }

        return result;
    }

    private HttpResponse<InputStream> sendHttpRequest(String url, HttpClient httpClient) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build();

        try {
            return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
        } catch (Exception e) {
            log.error("Error fetching logs: {}", e.getMessage());
            return null;
        }
    }

    private void processLogLines(
        HttpResponse<InputStream> response,
        LogService logService,
        LogsStatistic logsStatistic,
        List<LogFilter> filters
    ) {
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(response.body(), StandardCharsets.UTF_8))) {
            reader.lines().forEach(line -> {
                if (filters != null) {
                    logService.addLog(logService.parse(line), logsStatistic, filters);
                } else {
                    logService.addLog(logService.parse(line), logsStatistic);
                }
            });
        } catch (Exception e) {
            log.error("Error processing log lines: {}", e.getMessage());
        }
    }
}
