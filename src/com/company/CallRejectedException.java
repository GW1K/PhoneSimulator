package com.company;

/**
 * Exception that indicates if connection was rejected by destination phone.
 */
public class CallRejectedException extends Exception {

    /**
     * Class {@code CallRejectedException} constructor.
     * @param e Exception message.
     */
    CallRejectedException(String e) {
        super(e);
    }
}
