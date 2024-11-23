package backend.academy.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;

public record LogReport(
    int totalRequests,
    double averageResponseSize,
    double responseSize95Percentile,
    Map<String, Long> mostFrequentResources,
    Map<HttpStatus, Long> mostFrequentStatusCodes,
    LocalDateTime from,
    LocalDateTime to,
    List<String> sources
) {
}
