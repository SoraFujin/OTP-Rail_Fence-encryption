package com.encryption2.demo.Controllers.RailFenceController;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.encryption2.demo.utils.RailFence.RailFenceEncryption;
import com.encryption2.demo.utils.RailFence.RailFenceEncryptionRequest;

@RestController
@RequestMapping("/api")
public class RailFenceController {

    // Encrypt method that takes dynamic key from the frontend
    @PostMapping("/railfence/encrypt")
    public String encrypt(@RequestBody RailFenceEncryptionRequest request) {
        int key = request.getKey();  // Get the key from the frontend
        String plainText = request.getPlainText();
        
        if (key < 2) {
            return "Key must be greater than 1";
        }
        
        return RailFenceEncryption.encrypt(plainText, key);
    }

    // Decrypt method that takes dynamic key from the frontend
    @PostMapping("/railfence/decrypt")
    public String decrypt(@RequestBody RailFenceEncryptionRequest request) {
        int key = request.getKey();  // Get the key from the frontend
        String cipherText = request.getCipherText();
        
        if (key < 2) {
            return "Key must be greater than 1";
        }
        
        return RailFenceEncryption.decrypt(cipherText, key);
    }
}

