package ratelimiter.model;

import java.time.LocalDateTime;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

public class UserRequests {

    private final Deque<LocalDateTime> queue;

    public UserRequests() {
        queue = new LinkedList<>();
    }

    public synchronized void add(LocalDateTime localDateTime) {
        queue.add(
                Objects.requireNonNull(localDateTime)
        );
    }

    public synchronized void removeExpiredRequests(LocalDateTime current, TimeFrame timeFrame) {
        Objects.requireNonNull(current);
        Objects.requireNonNull(timeFrame);

        final var timeUnit = timeFrame.getChronoUnit();
        final var amount = timeFrame.getAmount();
        queue.removeIf(userRequestTime -> timeUnit.between(current, userRequestTime) > amount);
    }

    public int size() {
        return queue.size();
    }
}
