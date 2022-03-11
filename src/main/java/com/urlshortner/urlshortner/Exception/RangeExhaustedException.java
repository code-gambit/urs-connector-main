package com.urlshortner.urlshortner.Exception;

public class RangeExhaustedException extends Exception {

    @Override
    public String getMessage() {
        return "Range exhausted";
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }
}
