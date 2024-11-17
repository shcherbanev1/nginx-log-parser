package backend.academy.model;

import java.time.LocalDateTime;
import java.util.Map;

public record LogReport(
    int totalRequests,
    double averageResponseSize,
    double responseSize95Percentile,
    Map<String, Long> mostFrequentResources,
    Map<Integer, Long> mostFrequentStatusCodes,
    LocalDateTime from,
    LocalDateTime to
) {
}
