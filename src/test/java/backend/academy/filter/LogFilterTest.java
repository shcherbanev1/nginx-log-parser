package backend.academy.filter;

import backend.academy.model.LogRecord;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogFilterTest {

    private static LogFilter logFilter1;
    private static LogFilter logFilter2;
    private static LogFilter logFilter3;
    private static LogFilter logFilter4;
    private static LogFilter logFilter5;
    private static LogFilter logFilter6;
    private static LogRecord logRecord;

    @BeforeAll
    static void setUp() {
        logFilter1 = new FromDateFilter(LocalDate.of(2002, 1, 1));
        logFilter2 = new FromDateFilter(LocalDate.of(2020, 9, 9));
        logFilter3 = new ToDateFilter(LocalDate.of(2017, 1, 1));
        logFilter4 = new ToDateFilter(LocalDate.of(2005, 9, 9));
        logFilter5 = new EqualsFilter("method", "GET");
        logFilter6 = new EqualsFilter("method", "POST");

        logRecord = new LogRecord(
            "93.180.71.3",
            "-",
            LocalDateTime.parse("2015-05-17T08:05:32"),
            "GET",
            "/downloads/product_1",
            HttpStatus.resolve(304),
            0,
            "-",
            "Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)");
    }

    @Test
    void dateFilterFromInRange() {
        assertTrue(logFilter1.test(logRecord));
    }

    @Test
    void dateFilterFromOutOfRange() {
        assertFalse(logFilter2.test(logRecord));
    }

    @Test
    void dateFilterToInRange() {
        assertTrue(logFilter3.test(logRecord));
    }

    @Test
    void dateFilterToOutOfRange() {
        assertFalse(logFilter4.test(logRecord));
    }

    @Test
    void fieldFilterValid() {
        assertTrue(logFilter5.test(logRecord));
    }

    @Test
    void fieldFilterInvalid() {
        assertFalse(logFilter6.test(logRecord));
    }

}
