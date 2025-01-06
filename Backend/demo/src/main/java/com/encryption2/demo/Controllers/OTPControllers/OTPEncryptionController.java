package com.encryption2.demo.Controllers.OTPControllers;

import java.io.IOException;
import java.util.Base64;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.encryption2.demo.utils.OnTimePad.OTPEncryption;
import com.encryption2.demo.utils.OnTimePad.OTPEncryptionRequest;
import com.encryption2.demo.utils.OnTimePad.OTPEncryptionResponse;

@RestController
@RequestMapping("/api") // Base URL
public class OTPEncryptionController {

    // Encrypt message (Text)
    @PostMapping("/otp/encrypt")
    public OTPEncryptionResponse encryptText(@RequestBody OTPEncryptionRequest request) {
        // Generate a hashed key and ensure it's long enough for the plaintext
        String key = OTPEncryption.generateHashedKey(request.getPlainText());

        // Ensure key length is enough for the plaintext
        key = OTPEncryption.ensureKeyLength(key, request.getPlainText());

        // Encrypt the plain text using the extended key
        String cipherText = OTPEncryption.encrypt(request.getPlainText(), key);

        // Return the cipher text and the key
        return new OTPEncryptionResponse(cipherText, key);
    }

    // Encrypt file (Binary or Text)
    @PostMapping("/otp/encryptFile")
    public String encryptFile(@RequestParam("file") MultipartFile file, @RequestParam("key") String key) throws IOException {
        // Convert the file content to a string (text or binary)
        byte[] fileBytes = file.getBytes();
        String fileContent = new String(fileBytes);

        // Encrypt the file content using the key
        String encryptedContent = OTPEncryption.encrypt(fileContent, key);

        // Optionally, you can save the encrypted content to a file or return it directly
        return encryptedContent;
    }

    // Decrypt message (Text)
    @PostMapping("/otp/decrypt")
    public String decryptText(@RequestBody OTPEncryptionRequest request) {
        // Decode the Base64-encoded cipher text
        String decodedCipherText = new String(Base64.getDecoder().decode(request.getCipherText()));

        // Ensure the key is long enough for the cipher text
        String key = OTPEncryption.ensureKeyLength(request.getKey(), decodedCipherText);

        // Decrypt the cipher text with the provided key
        return OTPEncryption.decrypt(decodedCipherText, key);
    }

    // Decrypt file (Binary or Text)
    @PostMapping("/otp/decryptFile")
    public String decryptFile(@RequestParam("file") MultipartFile file, @RequestParam("key") String key) throws IOException {
        // Read the uploaded file as a string
        byte[] fileBytes = file.getBytes();
        String encryptedContent = new String(fileBytes);

        // Decode and decrypt the content
        String decryptedContent = OTPEncryption.decrypt(encryptedContent, key);

        // Optionally, save the decrypted content to a new file or return it directly
        return decryptedContent;
    }
}
