package backend.academy.model;

import lombok.Getter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public class HttpStatus {

    private static final Map<Integer, String> HTTP_STATUS_DESCRIPTIONS;
    private final int httpCode;
    private final String httpStatus;


    static {
        Map<Integer, String> statuses = new HashMap<>();
        statuses.put(200, "OK");
        statuses.put(201, "Created");
        statuses.put(204, "No Content");
        statuses.put(400, "Bad Request");
        statuses.put(401, "Unauthorized");
        statuses.put(403, "Forbidden");
        statuses.put(404, "Not Found");
        statuses.put(500, "Internal Server Error");
        statuses.put(502, "Bad Gateway");
        statuses.put(503, "Service Unavailable");
        HTTP_STATUS_DESCRIPTIONS = Collections.unmodifiableMap(statuses);
    }

    public HttpStatus(int httpCode) {
        this.httpCode = httpCode;
        this.httpStatus = HTTP_STATUS_DESCRIPTIONS.getOrDefault(httpCode, "Unknown status. Google it");
    }
}
