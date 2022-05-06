package ratelimiter;

import java.util.Objects;

import ratelimiter.model.Request;
import ratelimiter.model.TimeFrame;

public abstract class RateLimiter {
    protected final int rateLimit;
    protected final TimeFrame timeFrame;

    /**
     * @param limit non negative
     * @param timeFrame Time frame not null
     */
    protected RateLimiter(int limit, TimeFrame timeFrame) {
        this.rateLimit = limit;
        this.timeFrame = Objects.requireNonNull(timeFrame);
    }

    public abstract boolean hit(Request request);
}
