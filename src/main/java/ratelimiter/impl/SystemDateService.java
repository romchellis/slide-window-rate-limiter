package ratelimiter.impl;

import java.time.LocalDateTime;

/**
 * Uses local machine date time
 */
public class SystemDateService implements DateTimeService {

    @Override
    public LocalDateTime currentTime() {
        return LocalDateTime.now();
    }

}
