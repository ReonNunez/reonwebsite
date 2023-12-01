package com.sample.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class Hasher {
        public void main() {
        String password = "myPassword";
        
        // Hash the password and store it securely
        String hashedPassword = hashPassword(password, generateRandomSalt());

        // Check if a provided password matches the hashed password
        boolean isMatch = checkPassword("myPassword", hashedPassword, generateRandomSalt());

        System.out.println("Password matches: " + isMatch);
    }

    // Method to hash a password
    public static String hashPassword(String password, String salt) {
        try {

            // Create a message digest with the SHA-256 algorithm
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Add the salt to the password
            byte[] passwordBytes = (password + salt).getBytes();

            // Hash the password
            byte[] hashedPassword = digest.digest(passwordBytes);

            // Convert the hashed password to a hexadecimal string
            return bytesToHex(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            // Handle exception
            return null;
        }
    }

    // Method to check if a provided password matches the hashed password
    public static boolean checkPassword(String inputPassword, String hashedPassword, String salt) {
        return hashedPassword != null && hashedPassword.equals(hashPassword(inputPassword, salt));
    }


    // Helper method to convert bytes to a hexadecimal string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    public static String generateRandomSalt() {
        String saltCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int saltLength = 16; // You can adjust the length of the salt as needed
        SecureRandom random = new SecureRandom();
        StringBuilder salt = new StringBuilder(saltLength);

        for (int i = 0; i < saltLength; i++) {
            int randomIndex = random.nextInt(saltCharacters.length());
            char randomChar = saltCharacters.charAt(randomIndex);
            salt.append(randomChar);
        }

        return salt.toString();
    }
}
