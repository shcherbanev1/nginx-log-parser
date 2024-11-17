package backend.academy.model;

import com.google.common.math.Quantiles;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.Getter;

public class LogsStatistic {
    @Getter private int totalRequests;
    @Getter private LocalDateTime from;
    @Getter private LocalDateTime to;
    private final Map<String, Long> resourceCount;
    private final Map<Integer, Long> statusCodeCount;
    private long totalResponseSize;
    private final List<Integer> responseSizes;

    public LogsStatistic() {
        totalRequests = 0;
        resourceCount = new HashMap<>();
        statusCodeCount = new HashMap<>();
        totalResponseSize = 0;
        responseSizes = new LinkedList<>();
        from = null;
        to = null;
    }

    public Map<String, Long> getMostFrequentResources(int limit) {
        return resourceCount.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(limit)
            .collect(LinkedHashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), HashMap::putAll);
    }

    public Map<Integer, Long> getMostFrequentStatusCodes(int limit) {
        return statusCodeCount.entrySet().stream()
            .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
            .limit(limit)
            .collect(LinkedHashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), HashMap::putAll);
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

    public void incrementTotalRequests() {
        totalRequests++;
    }

    public void addResource(String resource) {
        resourceCount.merge(resource, 1L, Long::sum);
    }

    public void addStatusCode(int statusCode) {
        statusCodeCount.merge(statusCode, 1L, Long::sum);
    }

    public void addResponseSize(int bytes) {
        totalResponseSize += bytes;
        responseSizes.add(bytes);
    }

    public void updateDateFrom(LocalDateTime date) {
        if (from == null) {
            from = date;
            return;
        }
        if (date.isBefore(from)) {
            from = date;
        }
    }

    public void updateDateTo(LocalDateTime date) {
        if (to == null) {
            to = date;
            return;
        }
        if (date.isAfter(to)) {
            to = date;
        }
    }
}
