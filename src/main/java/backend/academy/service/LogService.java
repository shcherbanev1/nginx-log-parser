package backend.academy.service;

import backend.academy.filter.LogFilter;
import backend.academy.model.LogRecord;
import backend.academy.model.LogReport;
import backend.academy.model.LogsStatistic;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss",
        Locale.US);
    private static final Pattern NGINX_LOG_PATTERN = Pattern.compile(
        "(?<remoteAddr>\\S+) - (?<remoteUser>\\S+) \\[(?<dateTime>[^\\]]*)\\] \"(?<request>[^\"]*)\" "
        + "(?<httpCode>\\d{3}) (?<bytes>\\d+) \"(?<httpReferer>[^\"]*)\" \"(?<httpUserAgent>[^\"]*)\"");
    private static final int PERCENTILE = 95;
    private static final int LIMIT = 10;

    @SuppressWarnings("MultipleStringLiterals")
    public LogRecord parse(String log) {
        Matcher matcher = NGINX_LOG_PATTERN.matcher(log);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid log: " + log);
        }
        String remoteAddr = matcher.group("remoteAddr");
        String remoteUser = matcher.group("remoteUser");
        LocalDateTime localDate = LocalDateTime.parse(matcher.group("dateTime").split(" ")[0], DATE_TIME_FORMATTER);
        String method = matcher.group("request").split(" ")[0];
        String request = matcher.group("request").split(" ")[1];
        HttpStatus httpCode = HttpStatus.resolve((Integer.parseInt(matcher.group("httpCode"))));
        int bytes = Integer.parseInt(matcher.group("bytes"));
        String httpReferer = matcher.group("httpReferer");
        String httpUserAgent = matcher.group("httpUserAgent");
        return new LogRecord(remoteAddr, remoteUser, localDate, method, request, httpCode, bytes, httpReferer,
            httpUserAgent);
    }

    public void addLog(LogRecord logRecord, LogsStatistic stats) {
        stats.addLog(logRecord);
    }

    public void addLog(LogRecord logRecord, LogsStatistic stats, List<LogFilter> filters) {
        if (filters.stream().allMatch(it -> it.test(logRecord))) {
            addLog(logRecord, stats);
        }
    }

    public LogReport generateReport(LogsStatistic stats) {
        int totalRequests = stats.totalRequests();
        double averageResponseSize = stats.getAverageResponseSize();
        double responseSize95Percentile = stats.getResponseSizePercentile(PERCENTILE);
        Map<String, Long> topResources = stats.getMostFrequentResources(LIMIT);
        Map<HttpStatus, Long> topStatusCodes = stats.getMostFrequentStatusCodes(LIMIT);
        LocalDateTime from = stats.from();
        LocalDateTime to = stats.to();
        List<String> sources = stats.sources();

        return new LogReport(
            totalRequests,
            averageResponseSize,
            responseSize95Percentile,
            topResources,
            topStatusCodes,
            from,
            to,
            sources
        );
    }

}
