package com.company;

import java.time.Duration;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

/**
 * Class {@code PhoneSimulator} implements console user interface that allows user to simulate phones connections.
 */
public class PhoneSimulator {

    /**
     * List that contains phone objects generated or defined by user.
     */
    private LinkedList<Phone> phones;

    /**
     * Class {@code PhoneSimulator} constructor.
     */
    PhoneSimulator() {
        this.phones = new LinkedList<>();
    }

    /**
     * Method used to generate phone objects with random phone numbers and add them to {@code phones} list.
     * @param amount Number of phones to be generated.
     */
    public void generatePhones(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("invalid amount " + amount + " of phones to generate");
        }
        phones.clear();
        Random r = new Random();
        for (int i = 0; i < amount; i++) {
            String phoneNumber = String.format(
                    "%03d%03d%03d",
                    r.nextInt(400)+500,
                    r.nextInt(400)+500,
                    r.nextInt(1000));
            phones.add(new Phone(phoneNumber));
        }
    }

    /**
     * Method displays submenu for selected phone and handles its usage.
     * @param id Phone identifier in {@code phones} list.
     */
    public void handlePhone(int id) {
        if (id < 0 || id >= phones.size()) {
            throw new IndexOutOfBoundsException("invalid phone ID " + id);
        }
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        while (choice != 4) {
            displayPhoneMenu();
            System.out.print("(ID:"+ id + " " + phones.get(id)  + ")" + " select option: ");
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Try again ...");
                scanner.next();
            }
            switch (choice) {
                case 1: {
                    try {
                        System.out.print("Phone ID: ");
                        int destPhoneID = scanner.nextInt();
                        if (destPhoneID < 0 || destPhoneID >= phones.size()) {
                            throw new IndexOutOfBoundsException("invalid phone ID " + id);
                        }
                        System.out.print("Should call be accepted by destination? (Y/N): ");
                        String accept = scanner.next();
                        if (accept.equals("Y")) {
                            System.out.print("Conversation duration in seconds: ");
                            int duration = scanner.nextInt();
                            phones.get(id).call(phones.get(destPhoneID), true, Duration.ofSeconds(duration));
                        } else if(accept.equals("N")) {
                            phones.get(id).call(phones.get(destPhoneID), false, Duration.ofSeconds(0));
                        } else {
                            throw new IllegalArgumentException("given input doesn't match Y or N");
                        }
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Phone with given ID doesn't exist. Try again ...");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Given input doesn't match Y or N. Try again ...");
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Try again ...");
                        scanner.next();
                    }
                } break;
                case 2: {
                    phones.get(id).displayOutboundRegister();
                    phones.get(id).displayInboundRegister();
                } break;
                case 3: {
                    System.out.print("File name: ");
                    try {
                        String input = scanner.next("[\\w\\s\\.]+\\.[a-zA-Z]{3}");
                        phones.get(id).saveRegisterToFile(input);
                    } catch (InputMismatchException e) {
                        System.out.println("Given filename is invalid. Try again ...");
                        scanner.next();
                    }
                } break;
                case 4:
                    break;
                default: {
                    System.out.println("Given option doesn't exist. Try again ...");
                }
            }
        }
    }

    /**
     * Prints main menu of the program.
     */
    public void displayMainMenu() {
        System.out.println("1 - Generate phones");
        System.out.println("2 - Add phone");
        System.out.println("3 - Remove phone");
        System.out.println("4 - Select phone");
        System.out.println("5 - Display phones");
        System.out.println("0 - Exit");
    }

    /**
     * Prints submenu with supported operations for selected phone.
     */
    public void displayPhoneMenu() {
        System.out.println("1 - Call");
        System.out.println("2 - Display register");
        System.out.println("3 - Save register to file");
        System.out.println("4 - Go back");
    }

    /**
     * Prints current {@code phones} list content.
     */
    public void displayPhones() {
        System.out.println();
        System.out.println("Phones:");
        if (phones.size() == 0) {
            System.out.println("(Empty)");
            return;
        }
        for (int i = 0; i < phones.size(); i++) {
            System.out.println("ID:" + i + " " + phones.get(i));
        }
    }

    /**
     * Main program entry point. Handles main menu of the program.
     * @param args Not used.
     */
    public static void main(String[] args) {
        System.out.println("Phone Simulator");
        Scanner scanner = new Scanner(System.in);
        PhoneSimulator simulator = new PhoneSimulator();
        int choice = -1;
        while (choice != 0) {
            simulator.displayMainMenu();
            System.out.print("Select option: ");
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Try again ...");
                scanner.next();
            }
            switch (choice) {
                case 0:
                    break;
                case 1: {
                    System.out.print("Amount of phones to generate: ");
                    try {
                        int input = scanner.nextInt();
                        simulator.generatePhones(input);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid amount. Try again ...");
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Try again ...");
                        scanner.next();
                    }
                } break;
                case 2: {
                    System.out.print("Phone number: ");
                    try {
                        String input = scanner.next("[5-8]\\d{2}\\d{3}\\d{3}");
                        simulator.phones.add(new Phone(input));
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Try again ...");
                        scanner.next();
                    }
                } break;
                case 3: {
                    System.out.print("Phone ID: ");
                    try {
                        int input = scanner.nextInt();
                        simulator.phones.remove(input);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Phone with given ID doesn't exist. Try again ...");
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Try again ...");
                        scanner.next();
                    }
                } break;
                case 4: {
                    System.out.print("Phone ID: ");
                    try {
                        int input = scanner.nextInt();
                        simulator.handlePhone(input);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Phone with given ID doesn't exist. Try again ...");
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Try again ...");
                        scanner.next();
                    }
                } break;
                case 5: {
                    simulator.displayPhones();
                } break;
                default: {
                    System.out.println("Given option doesn't exist. Try again ...");
                }
            }
        }
    }
}
