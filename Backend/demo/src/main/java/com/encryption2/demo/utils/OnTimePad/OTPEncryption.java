package com.encryption2.demo.utils.OnTimePad;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

public class OTPEncryption {

    private String key;
    private String plainText;
    private String cipherText;

    public static void main(String[] args) throws IOException {
        String plainText = "Hello World";
        System.out.println("Plain text: " + plainText);

        String key = generateHashedKey(plainText);
        System.out.println("Key: " + key);

        String cipherText = encrypt(plainText, key);
        System.out.println("Cipher Text: " + cipherText);

        String message = decrypt(cipherText, key);
        System.out.println("Decrypted Message: " + message);

    }

    public OTPEncryption(String key, String plainText, String cipherText) {
        this.key = key;
        this.plainText = plainText;
        this.cipherText = cipherText;
    }

    //setter and getter methods
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

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

    public static String encrypt(String plainText, String key) {
        // Convert the plainText and key to UTF-8 encoded byte arrays
        byte[] plainTextBytes = plainText.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

        StringBuilder cipherText = new StringBuilder();

        // Ensure the key is at least as long as the plain text
        key = ensureKeyLength(new String(keyBytes, StandardCharsets.UTF_8), plainText);

        // XOR encryption with the UTF-8 encoded text and key
        for (int i = 0; i < plainTextBytes.length; i++) {
            byte plainByte = plainTextBytes[i];
            byte keyByte = keyBytes[i % keyBytes.length]; // Ensure key repeats if it's shorter than plainText
            byte cipherByte = (byte) (plainByte ^ keyByte); // XOR operation

            cipherText.append((char) cipherByte);
        }

        // Base64 encode the resulting cipher text
        return Base64.getEncoder().encodeToString(cipherText.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static String decrypt(String cipherText, String key) {
        // Decode the Base64 encoded cipher text into bytes
        byte[] cipherTextBytes = Base64.getDecoder().decode(cipherText);
        // Convert the key to UTF-8 encoded bytes
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

        StringBuilder plainText = new StringBuilder();

        // Ensure the key is at least as long as the cipher text
        key = ensureKeyLength(new String(keyBytes, StandardCharsets.UTF_8), cipherText);

        // XOR decryption with the UTF-8 encoded cipher text and key
        for (int i = 0; i < cipherTextBytes.length; i++) {
            byte cipherByte = cipherTextBytes[i];
            byte keyByte = keyBytes[i % keyBytes.length]; // Ensure key repeats if it's shorter than cipherText
            byte decryptedByte = (byte) (cipherByte ^ keyByte); // XOR operation

            plainText.append((char) decryptedByte);
        }

        // Return the decrypted plain text in UTF-8 format
        return new String(plainText.toString().getBytes(), StandardCharsets.UTF_8);
    }

    public static String generateHashedKey(String plainText) {
        List<String> data = new ArrayList<>();

        // 1. Add Current Date and Time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        data.add(LocalDateTime.now().format(formatter));

        // 2. Add System Uptime (Milliseconds)
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        data.add(String.valueOf(uptime));

        // 3. Add CPU Model (cross-platform solution)
        data.add(getCPUModel());

        // 4. Add Network Information (IP Address and MAC Address)
        data.add(getNetworkInfo());

        // 5. Add Random Number (SecureRandom for entropy)
        SecureRandom random = new SecureRandom();
        data.add(String.valueOf(random.nextInt(999999)));

        // 6. Add UUID for more randomness
        data.add(UUID.randomUUID().toString().replace("-", "").substring(0, 8));

        // 7. Add NanoTime for precision
        data.add(String.valueOf(System.nanoTime() % 10000));

        // 8. Add Environment Variable (User name for uniqueness)
        data.add(System.getProperty("user.name"));

        // 9. Add additional random data
        for (int i = 0; i < 5; i++) {
            data.add(UUID.randomUUID().toString().replace("-", ""));
        }

        // Shuffle the data to ensure randomness
        Collections.shuffle(data, new SecureRandom());

        // Build the final key by concatenating the data
        StringBuilder key = new StringBuilder();
        for (String part : data) {
            key.append(part);
        }

        // Hash the key using SHA-256
        String hashedKey = hashKey(key.toString());

        // Ensure that the key is at least as long as the plain text
        return ensureKeyLength(hashedKey, plainText);
    }

    private static String generateAdditionalRandomData() {
        // Use SecureRandom to ensure more randomness in extended key data
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32); // Generate a long random string
    }

    // SHA-256 Hashing Method
    private static String hashKey(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert byte array to hexadecimal format
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while hashing key", e);
        }
    }

    // Ensure the key is at least as long as the plain text
    public static String ensureKeyLength(String key, String plainText) {
        int plainTextLength = plainText.length();

        // If the key is shorter than the plain text, extend it with more random data
        if (key.length() < plainTextLength) {
            StringBuilder extendedKey = new StringBuilder(key);
            while (extendedKey.length() < plainTextLength) {
                // Add additional random data to the key
                extendedKey.append(generateAdditionalRandomData());
            }
            return extendedKey.toString().substring(0, plainTextLength); // Trim to exact length if necessary
        }

        // If the key is longer than the plain text, return the full key
        return key.substring(0, plainTextLength); // Return key with the same length as the plain text
    }

    private static String getCPUModel() {
        String cpuModel = "UnknownCPU";
        try {
            Process process = Runtime.getRuntime().exec("lscpu"); // For Linux/Mac
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Model name:") || line.contains("model name")) {
                    cpuModel = line.split(":")[1].trim();
                    break;
                }
            }
        } catch (IOException e) {
            cpuModel = "ErrorReadingCPU";
        }
        return cpuModel.replaceAll("\s", ""); // Remove spaces for consistency
    }

    private static String getNetworkInfo() {
        StringBuilder networkInfo = new StringBuilder();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();

                // Get MAC Address
                byte[] mac = networkInterface.getHardwareAddress();
                if (mac != null) {
                    for (byte b : mac) {
                        networkInfo.append(String.format("%02X", b));
                    }
                }

                // Get IP Addresses
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        networkInfo.append(inetAddress.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            networkInfo.append("ErrorReadingNetwork");
        }
        return networkInfo.toString();
    }
    // Encrypt a file and write the encrypted content to a new file

    public static String encryptFile(String filePath) throws IOException {
        File file = new File(filePath);
        boolean isBinaryFile = isBinaryFile(file);

        // Step 1: Read the entire file (binary or text)
        byte[] fileBytes = readFile(file, isBinaryFile);

        // Step 2: Generate a key (you can create a secure key generation logic)
        String key = OTPEncryption.generateHashedKey(new String(fileBytes)); // Use file content to generate key

        // Step 3: Encrypt the file content
        String encryptedText = OTPEncryption.encrypt(new String(fileBytes), key);

        // Step 4: Write the encrypted content to a new file
        writeEncryptedToFile(encryptedText, "path/to/your/output/encryptedFile.enc");

        // Step 5: Optionally, encode the result to Base64 before returning
        return Base64.getEncoder().encodeToString(encryptedText.getBytes()); // Return Base64 encoded encrypted text
    }

    // Check if a file is binary (based on a basic heuristic, e.g., presence of null byte)
    private static boolean isBinaryFile(File file) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            int byteRead;
            while ((byteRead = bis.read()) != -1) {
                if (byteRead == 0) {
                    return true; // Found a null byte, likely a binary file
                }
            }
        }
        return false;
    }

    // Read the file content (binary or text)
    private static byte[] readFile(File file, boolean isBinary) throws IOException {
        if (isBinary) {
            return readBinaryFile(file);
        } else {
            return readTextFile(file).getBytes();
        }
    }

    // Read text file content
    private static String readTextFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    // Read binary file content
    private static byte[] readBinaryFile(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return fis.readAllBytes();
        }
    }

    // Write the encrypted content to a new file
    public static void writeEncryptedToFile(String encryptedText, String outputFilePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write(encryptedText);
        }
    }

    public static void decryptFile(String encryptedFilePath, String decryptionKey, String outputFilePath) throws IOException {
        // Step 1: Read the encrypted file
        String encryptedText = readEncryptedFile(encryptedFilePath);

        // Step 2: Decrypt the content
        String decryptedContent = OTPEncryption.decrypt(encryptedText, decryptionKey);

        // Step 3: Write the decrypted content to a new file
        writeDecryptedToFile(decryptedContent, outputFilePath);
    }

    // Read the encrypted file content
    private static String readEncryptedFile(String encryptedFilePath) throws IOException {
        StringBuilder encryptedContent = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(encryptedFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                encryptedContent.append(line).append("\n");
            }
        }
        return encryptedContent.toString().trim();  // Remove extra newlines if any
    }

    // Write the decrypted content back to a new file (binary or text)
    private static void writeDecryptedToFile(String decryptedContent, String outputFilePath) throws IOException {
        // Assuming the decrypted content is text for simplicity
        // In case of binary, you will need to handle byte arrays and content type
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write(decryptedContent);
        }
    }
}
