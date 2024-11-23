package backend.academy.model;

import com.google.common.math.Quantiles;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public class LogsStatistic {
    @Getter private int totalRequests;
    @Getter private LocalDateTime from;
    @Getter private LocalDateTime to;
    private final Map<String, Long> resourceCount;
    private final Map<Integer, Long> statusCodeCount;
    private long totalResponseSize;
    private final List<Integer> responseSizes;
    @Getter private final List<String> sources;

    public LogsStatistic() {
        totalRequests = 0;
        resourceCount = new HashMap<>();
        statusCodeCount = new HashMap<>();
        totalResponseSize = 0;
        responseSizes = new LinkedList<>();
        from = null;
        to = null;
        sources = new LinkedList<>();
    }

    public void addLog(LogRecord logRecord) {
        incrementTotalRequests();
        addResource(logRecord.request());
        addStatusCode(logRecord.httpStatus().value());
        addResponseSize(logRecord.bytes());
        updateDateFrom(logRecord.localDate());
        updateDateTo(logRecord.localDate());
    }

    public Map<String, Long> getMostFrequentResources(int limit) {
        return resourceCount.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(limit)
            .collect(LinkedHashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), HashMap::putAll);
    }

    public Map<HttpStatus, Long> getMostFrequentStatusCodes(int limit) {
        return statusCodeCount.entrySet().stream()
            .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
            .limit(limit)
            .collect(LinkedHashMap::new, (m, e) -> m.put(HttpStatus.resolve(e.getKey()), e.getValue()),
                HashMap::putAll);
    }

    public double getAverageResponseSize() {
        return totalRequests == 0 ? 0 : (double) totalResponseSize / totalRequests;
    }

    public double getResponseSizePercentile(int percentile) {
        if (responseSizes.isEmpty()) {
            return 0;
        }
        return Quantiles.percentiles().index(percentile).compute(responseSizes);
    }

    private void incrementTotalRequests() {
        totalRequests++;
    }

    private void addResource(String resource) {
        resourceCount.merge(resource, 1L, Long::sum);
    }

    private void addStatusCode(int statusCode) {
        statusCodeCount.merge(statusCode, 1L, Long::sum);
    }

    private void addResponseSize(int bytes) {
        totalResponseSize += bytes;
        responseSizes.add(bytes);
    }

    private void updateDateFrom(LocalDateTime date) {
        if (from == null) {
            from = date;
            return;
        }
        if (date.isBefore(from)) {
            from = date;
        }
    }

    private void updateDateTo(LocalDateTime date) {
        if (to == null) {
            to = date;
            return;
        }
        if (date.isAfter(to)) {
            to = date;
        }
    }

    public void addSource(String resourceName) {
        sources.add(resourceName);
    }

}
