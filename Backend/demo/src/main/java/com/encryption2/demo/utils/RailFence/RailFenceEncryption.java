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

        char newlinePlaceholder = (char) 0x1A;  
        plainText = plainText.replace("\n", String.valueOf(newlinePlaceholder));  

        char[][] rail = new char[key][plainText.length()];

        for (int i = 0; i < key; i++) {
            for (int j = 0; j < plainText.length(); j++) {
                rail[i][j] = '\n';
            }
        }

        int row = 0;
        int col = 0;
        boolean goingDown = false;

        for (int i = 0; i < plainText.length(); i++) {
            char currentChar = plainText.charAt(i);
            rail[row][col++] = currentChar;  

            if (row == 0 || row == key - 1) {
                goingDown = !goingDown;  
            }

            row = goingDown ? row + 1 : row - 1;  
        }

        StringBuilder cipherText = new StringBuilder();
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < plainText.length(); j++) {
                if (rail[i][j] != '\n') {
                    cipherText.append(rail[i][j]);  
                }
            }
        }

        return cipherText.toString();
    }

    public static String decrypt(String cipherText, int key) {
        if (key < 2) {
            return "Key must be greater than 1";
        }

        char newlinePlaceholder = (char) 0x1A;  

        char[][] rail = new char[key][cipherText.length()];

        for (int i = 0; i < key; i++) {
            for (int j = 0; j < cipherText.length(); j++) {
                rail[i][j] = '\n';
            }
        }

        int row = 0;
        int col = 0;
        boolean goingDown = false;

        for (int i = 0; i < cipherText.length(); i++) {
            rail[row][col++] = '*';  

            if (row == 0 || row == key - 1) {
                goingDown = !goingDown;
            }

            row = goingDown ? row + 1 : row - 1; 
        }

        int index = 0;
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < cipherText.length(); j++) {
                if (rail[i][j] == '*' && index < cipherText.length()) {
                    rail[i][j] = cipherText.charAt(index++);  
                }
            }
        }

        StringBuilder plainText = new StringBuilder();
        row = 0;
        col = 0;
        goingDown = false;

        for (int i = 0; i < cipherText.length(); i++) {
            char currentChar = rail[row][col++];

            if (currentChar != newlinePlaceholder) {
                plainText.append(currentChar); 
            }

            if (currentChar == newlinePlaceholder) {
                plainText.append('\n');  
            }

            if (row == 0 || row == key - 1) {
                goingDown = !goingDown;  
            }

            row = goingDown ? row + 1 : row - 1;  
        }

        return plainText.toString();
    }

}
