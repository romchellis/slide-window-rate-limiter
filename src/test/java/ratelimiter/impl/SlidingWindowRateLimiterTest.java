package ratelimiter.impl;

import static java.time.LocalDateTime.of;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

import ratelimiter.RateLimiter;
import ratelimiter.model.Request;
import ratelimiter.model.TimeFrame;
import ratelimiter.model.UserRequests;

public class SlidingWindowRateLimiterTest {

    public static final String USER_ID = "1";
    private RateLimiter rateLimiter;

    @Test
    void should_reject_request_if_more_than_limit() {
        var time = of(2000, 1, 1, 1, 1);
        var userIdToRequests = createTwoMockRequests(time, USER_ID);
        var limit = 2;
        var timeFrame = new TimeFrame(1l, ChronoUnit.MINUTES);
        var request = new Request(time, USER_ID);
        rateLimiter = new SlidingWindowRateLimiter(limit, timeFrame, userIdToRequests, () -> time);

        boolean hit = rateLimiter.hit(request);

        assertFalse(hit);
    }

    @Test
    void should_allow_request_if_less_than_limit() {
        var time = of(2000, 1, 1, 1, 1);
        var userIdToRequests = createTwoMockRequests(time, USER_ID);
        var limit = 3;
        var timeFrame = new TimeFrame(1l, ChronoUnit.MINUTES);
        var request = new Request(time, USER_ID);
        rateLimiter = new SlidingWindowRateLimiter(limit, timeFrame, userIdToRequests, () -> time);

        boolean hit = rateLimiter.hit(request);

        assertTrue(hit, "Expected limit was less than count of requests, but limiter rejected " +
                "request");
    }

    private HashMap<String, UserRequests> createTwoMockRequests(LocalDateTime time, String userId) {
        var userIdToRequests = new HashMap<String, UserRequests>();
        UserRequests userRequests = new UserRequests();
        userRequests.add(time.plusSeconds(1L));
        userRequests.add(time.plusSeconds(2L));
        userIdToRequests.put(userId, userRequests);
        return userIdToRequests;
    }

}