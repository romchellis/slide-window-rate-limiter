package ratelimiter.impl;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import ratelimiter.RateLimiter;
import ratelimiter.model.Request;
import ratelimiter.model.TimeFrame;
import ratelimiter.model.UserRequests;

public class SlidingWindowRateLimiter extends RateLimiter {

    private final DateTimeService dateTimeService;
    private final Map<String, UserRequests> storage;

    public SlidingWindowRateLimiter(int limit,
                                    TimeFrame timeFrame,
                                    DateTimeService dateTimeService) {
        this(limit, timeFrame, new ConcurrentHashMap<>(), dateTimeService);
    }

    SlidingWindowRateLimiter(int limit,
                             TimeFrame timeFrame,
                             Map<String, UserRequests> storage,
                             DateTimeService dateTimeService) {
        super(limit, timeFrame);
        this.storage = storage;
        this.dateTimeService = dateTimeService;
    }

    @Override
    public boolean hit(Request request) {
        Objects.requireNonNull(request);

        final var userId = request.getUserId();
        final var current = dateTimeService.currentTime();
        final var userRequests = storage.compute(userId, (k, v) -> v == null ? new UserRequests() : v);
        synchronized (userRequests) {
            userRequests.removeExpiredRequests(current, timeFrame);
            userRequests.add(request.getTime());
            return userRequests.size() <= rateLimit;
        }
    }

}
