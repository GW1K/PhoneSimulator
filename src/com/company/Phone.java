package com.company;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Class {@code Phone} stores data about phones and allows to simulate their behaviour.
 */
public class Phone {

    /**
     * Filed {@code phoneNumber} is used to identify phones.
     */
    private String phoneNumber;
    /**
     * Field {@code available} determines if phone is during conversation.
     */
    private boolean available;
    /**
     * Field {@code inboundRegister} stores information about incoming calls.
     */
    private final LinkedList<PhoneRegisterEntry> inboundRegister;
    /**
     * Field {@code outboundRegister} stores information about outgoing calls.
     */
    private final LinkedList<PhoneRegisterEntry> outboundRegister;

    /**
     * Class {@code Phone} constructor.
     * @param phoneNumber Gets phone number.
     */
    public Phone(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.available = true;
        this.inboundRegister = new LinkedList<>();
        this.outboundRegister = new LinkedList<>();
    }

    /**
     * Method that simulates phone calls and stores information about them in phone register.
     * Depending on phone state it behaves differently.
     * @param phone Destination phone object to be called.
     * @param accept Flag indicating if destination phone should accept call.
     * @param conversationTime Value of this parameter is used when {@code accept} flag is true.
     *                         It tells how long the conversation will be.
     */
    public void call(Phone phone, boolean accept, Duration conversationTime) {
        try {
            if (this.available) {
                if (phone.isAvailable()) {
                    outboundRegister.addFirst(new PhoneRegisterEntry(
                            phone.getPhoneNumber(),
                            accept,
                            true,
                            LocalDateTime.now(),
                            conversationTime
                    ));
                    this.available = false;
                    phone.receiveCall(this, accept, conversationTime);
                } else {
                    outboundRegister.addFirst(new PhoneRegisterEntry(
                            phone.getPhoneNumber(),
                            false,
                            false,
                            LocalDateTime.now(),
                            Duration.ofSeconds(0)
                    ));
                    phone.inboundRegister.addFirst(new PhoneRegisterEntry(
                            this.phoneNumber,
                            false,
                            false,
                            LocalDateTime.now(),
                            Duration.ofSeconds(0)
                    ));
                    throw new PhoneUnavailableException(this + " phone number " +
                            phoneNumber + " is currently unavailable");
                }
            } else {
                throw new PhoneUnavailableException(this + " is already during the conversation");
            }
        } catch (PhoneUnavailableException | CallRejectedException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method invoked by {@code call} when both phones are available and are ready to connect.
     * It simulates conversations between phones which will be unavailable during connection for other phones.
     * @param from Source phone object that is calling.
     * @param accept Flag indicates if destination phone should accept call.
     * @param conversationTime Passed through {@code call} method. Tells how long both phones will be unavailable.
     * @throws CallRejectedException If flag {@code accept} is false.
     */
    private void receiveCall(Phone from, boolean accept, Duration conversationTime) throws CallRejectedException {
        inboundRegister.addFirst(new PhoneRegisterEntry(
                from.getPhoneNumber(),
                accept,
                true,
                LocalDateTime.now(),
                conversationTime
        ));
        if (accept) {
            this.available = false;
            System.out.println(this + " accepted call from " + from.phoneNumber +
                    ". Conversation will last for " + conversationTime.getSeconds() + "s ...");
            Thread conversationThread = new Thread(() -> {
                try {
                    Thread.sleep(conversationTime.getSeconds() * 1000);
                    this.available = true;
                    from.available = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            conversationThread.setDaemon(true);
            conversationThread.start();
        } else {
            this.available = true;
            from.available = true;
            throw new CallRejectedException(this + " rejected call from " + from.phoneNumber);
        }
    }

    /**
     * Allows user to store phone register entries to a file with given name.
     * @param fileName File name with extension.
     */
    public void saveRegisterToFile(String fileName) {
        try {
            FileWriter fis = new FileWriter(fileName);
            BufferedWriter bw = new BufferedWriter(fis);
            bw.write(this.toString());
            bw.newLine();
            bw.write("[Outbound calls]");
            bw.newLine();
            outboundRegister.forEach(entry -> {
                try {
                    bw.write(entry.toString());
                    bw.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            bw.newLine();
            bw.write("[Inbound calls]");
            bw.newLine();
            inboundRegister.forEach(entry -> {
                try {
                    bw.write(entry.toString());
                    bw.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            bw.newLine();
            bw.flush();
            bw.close();
            fis.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Phone number getter.
     * @return Phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Phone number setter.
     * @param phoneNumber Phone number.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Checks {@code available} flag.
     * @return {@code available} flag.
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Inbound phone register getter.
     * @return Inbound phone register.
     */
    public List<PhoneRegisterEntry> getInboundRegister() {
        return inboundRegister;
    }

    /**
     * Outbound phone register getter.
     * @return Outbound phone register.
     */
    public List<PhoneRegisterEntry> getOutboundRegister() {
        return outboundRegister;
    }

    /**
     * Prints outgoing call entries from phone's register.
     */
    public void displayOutboundRegister() {
        System.out.println("Outbound register:");
        if (outboundRegister.size() == 0) {
            System.out.println("(Empty)");
            return;
        }
        outboundRegister.forEach(System.out::println);
    }

    /**
     * Prints incoming call entries from call's register.
     */
    public void displayInboundRegister() {
        System.out.println("Inbound register:");
        if (inboundRegister.size() == 0) {
            System.out.println("(Empty)");
            return;
        }
        inboundRegister.forEach(System.out::println);
    }

    @Override
    public String toString() {
        return "Phone{" + phoneNumber + '}';
    }
}
