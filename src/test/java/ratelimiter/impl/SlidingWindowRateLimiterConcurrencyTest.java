package ratelimiter.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import org.jooq.lambda.Unchecked;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import ratelimiter.RateLimiter;
import ratelimiter.model.Request;
import ratelimiter.model.TimeFrame;

public class SlidingWindowRateLimiterConcurrencyTest {

    public static final String USER_ID = "1";

    private RateLimiter rateLimiter;

    @Test
    void should_works_concurrently_as_sequentially() {
        var threadsCount = 10;
        var limit = 5;
        var requestResults = makeParralelRequests(threadsCount, limit);

        assertThat(requestResults).containsExactly(
                entry(false, 5l),
                entry(true, 5l)
        );
    }

    private Map<Boolean, Long> makeParralelRequests(int threadsCount, int limit) {
        var executor = Executors.newFixedThreadPool(threadsCount);
        var timeFrame = new TimeFrame(1l, ChronoUnit.MINUTES);
        var time = LocalDateTime.of(2000, 1, 1, 1, 1);
        var request = new Request(time, USER_ID);
        rateLimiter = new SlidingWindowRateLimiter(limit, timeFrame, () -> time);
        var list = new ArrayList<Future<Boolean>>();
        var barrier = new CyclicBarrier(threadsCount);

        for (int i = 0; i < threadsCount; i++) {
            var requestResult = executor.submit(() -> {
                barrier.await();
                return rateLimiter.hit(request);
            });
            list.add(requestResult);
        }

        executor.shutdown();
        Unchecked.runnable(() -> executor.awaitTermination(1l, TimeUnit.MINUTES));
        return groupRequestsBySucess(list);
    }

    private Map<Boolean, Long> groupRequestsBySucess(ArrayList<Future<Boolean>> list) {
        return list.stream()
                .map(success -> Unchecked.supplier(success::get).get())
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));
    }
}