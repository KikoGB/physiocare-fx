package com.gadeadiaz.physiocare.utils;

/**
 * Utility class that provides methods for validating user input.
 * <p>
 * This class contains validation methods for login and password inputs.
 * It ensures that the provided values meet specific criteria before proceeding.
 */
public class Validations {

    /**
     * Validates the login identifier (username) provided by the user.
     * <p>
     * The login is considered valid if it is not empty.
     *
     * @param login the login identifier to validate
     * @return true if the login is valid, false otherwise
     */
    public static boolean validateLogin(String login) {
        if (login.isEmpty()) {
            Message.showError("Login Error", "Admin identifier is not valid.");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Validates the password provided by the user.
     * <p>
     * The password is considered valid if its length is at least 7 characters.
     *
     * @param password the password to validate
     * @return true if the password is valid, false otherwise
     */
    public static boolean validatePassword(String password) {
        if (password.chars().count() >= 7) {
            return true;
        } else {
            Message.showError("Invalid password.", "The password entered is not valid.");
            return false;
        }
    }
}
