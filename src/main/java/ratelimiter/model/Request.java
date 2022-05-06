package ratelimiter.model;

import java.time.LocalDateTime;

public class Request {
    /**
     * Request time
     */
    private final LocalDateTime time;
    /**
     * Unique identifier for specific user
     */
    private final String userId;

    public Request(LocalDateTime time, String userId) {
        this.time = time;
        this.userId = userId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getUserId() {
        return userId;
    }
}
