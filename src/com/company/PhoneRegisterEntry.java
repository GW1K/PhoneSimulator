package com.company;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class used as phone register's list items to store data about calls.
 */
public class PhoneRegisterEntry {

    /**
     * Field {@code phoneNumber} contains destination phone number.
     */
    private final String phoneNumber;
    /**
     * Flag {@code accepted} is used to determine if destination phone accepted or rejected the call.
     */
    private final boolean accepted;
    /**
     * Flag {@code available} is used to indicate if destination phone is available.
     */
    private final boolean available;
    /**
     * Field {@code callDateTime} stores call's local date and time.
     */
    private final LocalDateTime callDateTime;
    /**
     * Field {@code conversationTime} defines call duration which is used to simulate connections.
     */
    private final Duration conversationTime;

    /**
     * Class {@code PhoneRegisterEntry} constructor.
     * @param phoneNumber Destination phone number.
     * @param accepted Flag that means if call will be accepted or rejected by destination.
     * @param available Flag indicating if destination phone is available.
     * @param callDate Call's local date and time.
     * @param conversationTime Call duration.
     */
    public PhoneRegisterEntry(String phoneNumber, boolean accepted, boolean available,
                              LocalDateTime callDate, Duration conversationTime) {
        this.phoneNumber = phoneNumber;
        this.accepted = accepted;
        this.available = available;
        this.callDateTime = callDate;
        this.conversationTime = conversationTime;
    }

    /**
     * Phone number getter.
     * @return Destination phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Accepted flag getter.
     * @return True if call state is accepted or false if rejected.
     */
    public boolean isAccepted() {
        return accepted;
    }

    /**
     * Call's local date and time getter.
     * @return Class {@code LocalDateTime} object.
     */
    public LocalDateTime getCallDate() {
        return callDateTime;
    }

    /**
     * Call's duration getter.
     * @return Class {@code Duration} object.
     */
    public Duration getConversationTime() {
        return conversationTime;
    }

    @Override
    public String toString() {
        return "{" + phoneNumber +
                ", " + (accepted?"Accepted":"Rejected") +
                ", " + (available?"Available":"Unavailable") +
                ", " + callDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
                ", " + conversationTime +
                '}';
    }
}
