package backend.academy.service;

import backend.academy.filter.EqualsFilter;
import backend.academy.filter.FromDateFilter;
import backend.academy.filter.LogFilter;
import backend.academy.model.LogRecord;
import backend.academy.model.LogReport;
import backend.academy.model.LogsStatistic;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LogServiceTest {

    private LogService logService;

    @BeforeEach
    void setUp() {
        logService = new LogService();
    }

    @Test
    void transformValidStringToLogRecord() {
        LogRecord actual = logService.parse(
            "93.180.71.3 - - [17/May/2015:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"");
        LogRecord expected = new LogRecord(
            "93.180.71.3",
            "-",
            LocalDateTime.parse("2015-05-17T08:05:32"),
            "GET",
            "/downloads/product_1",
            HttpStatus.resolve(304),
            0,
            "-",
            "Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)");
        assertEquals(expected, actual);
    }

    @Test
    void transformInvalidStringToLogRecord() {
        assertThrows(IllegalArgumentException.class, () -> logService.parse("not nginx log"));
    }

    @Test
    void checkReport() {
        LogsStatistic logsStatistic = new LogsStatistic();
        LogFilter methodFilter = new EqualsFilter("method", "GET");
        LogFilter fromFilter = new FromDateFilter(LocalDate.of(2017, 1,1));
        List<LogFilter> filters = List.of(methodFilter, fromFilter);
        var logs = getLogs();
        logs.forEach(it -> logService.addLog(it, logsStatistic, filters));
        LogReport actual = logService.generateReport(logsStatistic);
        assert HttpStatus.resolve(200) != null;
        assert HttpStatus.resolve(200) != null;
        assert HttpStatus.resolve(200) != null;
        LogReport expected = new LogReport(
            1,
            150,
            150,
            Map.of("/downloads/product_1", 1L),
            Map.of(HttpStatus.OK, 1L),
            LocalDateTime.parse("2020-05-17T08:05:32"),
            LocalDateTime.parse("2020-05-17T08:05:32"),
            List.of()
        );
        assertEquals(expected, actual);
    }

    private List<LogRecord> getLogs() {
        return List.of(
            new LogRecord(
                "93.180.71.3",
                "-",
                LocalDateTime.parse("2015-05-17T08:05:32"),
                "GET",
                "/downloads/product_1",
                HttpStatus.resolve(304),
                0,
                "-",
                "Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)"),
            new LogRecord(
                "93.181.72.3",
                "-",
                LocalDateTime.parse("2020-05-17T08:05:32"),
                "GET",
                "/downloads/product_1",
                HttpStatus.resolve(200),
                150,
                "-",
                "Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)"),
            new LogRecord(
                "23.83.171.23",
                "-",
                LocalDateTime.parse("2022-05-17T08:05:32"),
                "POST",
                "/downloads/product_2",
                HttpStatus.resolve(200),
                230,
                "-",
                "Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)")
        );
    }

}
