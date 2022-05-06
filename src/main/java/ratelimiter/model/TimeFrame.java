package ratelimiter.model;

import java.time.temporal.ChronoUnit;
import java.util.Objects;

import ratelimiter.common.Validation;

public class TimeFrame {

    private final long amount;
    private final ChronoUnit chronoUnit;

    public TimeFrame(long amount, ChronoUnit chronoUnit) {
        Validation.requirePositive(amount, "Amount of time cannot be negative");
        this.amount = Objects.requireNonNull(amount,"Amount of time cannot be null!");
        this.chronoUnit = Objects.requireNonNull(chronoUnit,"Chronounit of time cannot be null!");
    }

    public long getAmount() {
        return amount;
    }

    public ChronoUnit getChronoUnit() {
        return chronoUnit;
    }
}
