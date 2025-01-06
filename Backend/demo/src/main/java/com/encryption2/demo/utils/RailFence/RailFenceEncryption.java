package com.encryption2.demo.utils.RailFence;

public class RailFenceEncryption {

    private String plainText;
    private String cipherText;
    private int key;

    public RailFenceEncryption(String plainText, String cipherText, int key) {
        this.plainText = plainText;
        this.cipherText = cipherText;
        this.key = key;
    }

    public static void main(String[] args) {
        String plainText = "Hello World?\nthis is a secret message\nahmad";
        System.out.println("Plain text: " + plainText);
        int key = 2;
        String cipherText = encrypt(plainText, key);
        System.out.println("Cipher Text: " + cipherText);

        String decryptionText = decrypt(cipherText, key);
        System.out.println("Decrypted text: " + decryptionText);

    }

    // Getter and Setter methods
    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    public String getCipherText() {
        return cipherText;
    }

    public void setCipherText(String cipherText) {
        this.cipherText = cipherText;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public static String encrypt(String plainText, int key) {
        if (key < 2) {
            return "Key must be greater than 1";
        }

        // Use a special character for newline characters (SUB: 0x1A)
        char newlinePlaceholder = (char) 0x1A;  // Using SUB character as a placeholder
        plainText = plainText.replace("\n", String.valueOf(newlinePlaceholder));  // Replace newlines with placeholder

        char[][] rail = new char[key][plainText.length()];

        // Initialize the rail with '\n' (newline) characters
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < plainText.length(); j++) {
                rail[i][j] = '\n';
            }
        }

        int row = 0;
        int col = 0;
        boolean goingDown = false;

        // Fill the rail with characters from the plainText
        for (int i = 0; i < plainText.length(); i++) {
            char currentChar = plainText.charAt(i);
            rail[row][col++] = currentChar;  // Place characters in rail

            if (row == 0 || row == key - 1) {
                goingDown = !goingDown;  // Change direction when we reach the top or bottom
            }

            row = goingDown ? row + 1 : row - 1;  // Move up or down based on the flag
        }

        // Build the cipher text by reading the rail row by row
        StringBuilder cipherText = new StringBuilder();
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < plainText.length(); j++) {
                if (rail[i][j] != '\n') {
                    cipherText.append(rail[i][j]);  // Append non-newline characters to the result
                }
            }
        }

        return cipherText.toString();
    }

    public static String decrypt(String cipherText, int key) {
        if (key < 2) {
            return "Key must be greater than 1";
        }

        // Use the same special character for newlines as in the encryption process
        char newlinePlaceholder = (char) 0x1A;  // The same SUB character used for newline placeholders

        char[][] rail = new char[key][cipherText.length()];

        // Initialize the rail with '\n' (newline) characters
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < cipherText.length(); j++) {
                rail[i][j] = '\n';
            }
        }

        int row = 0;
        int col = 0;
        boolean goingDown = false;

        // Mark the positions where the characters will go (using '*' placeholders)
        for (int i = 0; i < cipherText.length(); i++) {
            rail[row][col++] = '*';  // Placeholder for cipher text

            if (row == 0 || row == key - 1) {
                goingDown = !goingDown;
            }

            row = goingDown ? row + 1 : row - 1;  // Move up or down based on the flag
        }

        int index = 0;
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < cipherText.length(); j++) {
                if (rail[i][j] == '*' && index < cipherText.length()) {
                    rail[i][j] = cipherText.charAt(index++);  // Replace '*' with actual character
                }
            }
        }

        StringBuilder plainText = new StringBuilder();
        row = 0;
        col = 0;
        goingDown = false;

        // Read the characters from the rail to reconstruct the plain text
        for (int i = 0; i < cipherText.length(); i++) {
            char currentChar = rail[row][col++];

            // Skip the newline placeholder during decryption
            if (currentChar != newlinePlaceholder) {
                plainText.append(currentChar);  // Append regular characters
            }

            if (currentChar == newlinePlaceholder) {
                plainText.append('\n');  // Convert the placeholder back to a newline
            }

            if (row == 0 || row == key - 1) {
                goingDown = !goingDown;  // Change direction when we reach the top or bottom
            }

            row = goingDown ? row + 1 : row - 1;  // Move up or down based on the flag
        }

        return plainText.toString();
    }

}
