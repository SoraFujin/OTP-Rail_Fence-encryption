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
        byte[] plainTextBytes = plainText.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        
        StringBuilder cipherText = new StringBuilder();
        
        for (int i = 0; i < plainTextBytes.length; i++) {
            byte plainByte = plainTextBytes[i];
            byte keyByte = keyBytes[i % keyBytes.length]; 
            byte cipherByte = (byte) (plainByte ^ keyByte); 

            cipherText.append((char) cipherByte);
        }

        return Base64.getEncoder().encodeToString(cipherText.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static String decrypt(String cipherText, String key) {
        byte[] cipherTextBytes = Base64.getDecoder().decode(cipherText);
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

        StringBuilder plainText = new StringBuilder();

        for (int i = 0; i < cipherTextBytes.length; i++) {
            byte cipherByte = cipherTextBytes[i];
            byte keyByte = keyBytes[i % keyBytes.length]; 
            byte decryptedByte = (byte) (cipherByte ^ keyByte); 

            plainText.append((char) decryptedByte);
        }

        return new String(plainText.toString().getBytes(), StandardCharsets.UTF_8);
    }

    public static String generateHashedKey(String plainText) {
        List<String> data = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        data.add(LocalDateTime.now().format(formatter));

        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        data.add(String.valueOf(uptime));

        data.add(getCPUModel());

        data.add(getNetworkInfo());

        SecureRandom random = new SecureRandom();
        data.add(String.valueOf(random.nextInt(999999)));

        data.add(UUID.randomUUID().toString().replace("-", "").substring(0, 8));

        data.add(String.valueOf(System.nanoTime() % 10000));

        data.add(System.getProperty("user.name"));

        for (int i = 0; i < 5; i++) {
            data.add(UUID.randomUUID().toString().replace("-", ""));
        }

        Collections.shuffle(data, new SecureRandom());

        StringBuilder key = new StringBuilder();
        for (String part : data) {
            key.append(part);
        }

        String hashedKey = hashKey(key.toString());

        return ensureKeyLength(hashedKey, plainText);
    }

    private static String generateAdditionalRandomData() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32); 
    }

    private static String hashKey(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

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

    public static String ensureKeyLength(String key, String plainText) {
        int plainTextLength = plainText.length();

        if (key.length() < plainTextLength) {
            StringBuilder extendedKey = new StringBuilder(key);
            while (extendedKey.length() < plainTextLength) {
                extendedKey.append(generateAdditionalRandomData());
            }
            return extendedKey.toString().substring(0, plainTextLength); 
        }

        return key.substring(0, plainTextLength); 
    }

    private static String getCPUModel() {
        String cpuModel = "UnknownCPU";
        try {
            Process process = Runtime.getRuntime().exec("lscpu"); 
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
        return cpuModel.replaceAll("\s", "");
    }

    private static String getNetworkInfo() {
        StringBuilder networkInfo = new StringBuilder();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();

                byte[] mac = networkInterface.getHardwareAddress();
                if (mac != null) {
                    for (byte b : mac) {
                        networkInfo.append(String.format("%02X", b));
                    }
                }

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

    public static String encryptFile(String filePath) throws IOException {
        File file = new File(filePath);
        boolean isBinaryFile = isBinaryFile(file);

        byte[] fileBytes = readFile(file, isBinaryFile);

        String key = OTPEncryption.generateHashedKey(new String(fileBytes)); // Use file content to generate key

        String encryptedText = OTPEncryption.encrypt(new String(fileBytes), key);

        writeEncryptedToFile(encryptedText, "path/to/your/output/encryptedFile.enc");

        return Base64.getEncoder().encodeToString(encryptedText.getBytes()); // Return Base64 encoded encrypted text
    }

    private static boolean isBinaryFile(File file) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            int byteRead;
            while ((byteRead = bis.read()) != -1) {
                if (byteRead == 0) {
                    return true; 
                }
            }
        }
        return false;
    }

    private static byte[] readFile(File file, boolean isBinary) throws IOException {
        if (isBinary) {
            return readBinaryFile(file);
        } else {
            return readTextFile(file).getBytes();
        }
    }

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

    private static byte[] readBinaryFile(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return fis.readAllBytes();
        }
    }

    public static void writeEncryptedToFile(String encryptedText, String outputFilePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write(encryptedText);
        }
    }

    public static void decryptFile(String encryptedFilePath, String decryptionKey, String outputFilePath) throws IOException {
        String encryptedText = readEncryptedFile(encryptedFilePath);

        String decryptedContent = OTPEncryption.decrypt(encryptedText, decryptionKey);

        writeDecryptedToFile(decryptedContent, outputFilePath);
    }

    private static String readEncryptedFile(String encryptedFilePath) throws IOException {
        StringBuilder encryptedContent = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(encryptedFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                encryptedContent.append(line).append("\n");
            }
        }
        return encryptedContent.toString().trim();  
    }

    private static void writeDecryptedToFile(String decryptedContent, String outputFilePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write(decryptedContent);
        }
    }
}