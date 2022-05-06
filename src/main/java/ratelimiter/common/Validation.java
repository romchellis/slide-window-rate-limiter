package ratelimiter.common;

import ratelimiter.exception.ValidationException;

public class Validation {

    public static long requirePositive(long number, String message) {
        if (number < 0)
            throw new ValidationException(message);
        return number;
    }

}
