package com.company;

/**
 * Exception used when destination phone isn't available.
 */
public class PhoneUnavailableException extends Exception {

    /**
     * Class {@code PhoneUnavailableException} constructor.
     * @param e Exception message.
     */
    PhoneUnavailableException(String e) {
        super(e);
    }
}
