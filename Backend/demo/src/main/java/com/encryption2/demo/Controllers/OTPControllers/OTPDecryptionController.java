// package com.encryption2.demo.Controllers.OTPControllers;

// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.encryption2.demo.utils.OnTimePad.OTPEncryption;
// import com.encryption2.demo.utils.OnTimePad.OTPEncryptionRequest;

// @RestController
// @RequestMapping("/api") // Base URL
// public class OTPDecryptionController {

//     // Decrypt message
//     @PostMapping("/otp/decrypt")
//     public String decryptText(@RequestBody OTPEncryptionRequest request) {
//         // Ensure the key is long enough for the cipher text
//         String key = OTPEncryption.ensureKeyLength(request.getKey(), request.getCipherText());
        
//         // Decrypt the cipher text with the provided key
//         return OTPEncryption.decrypt(request.getCipherText(), key);
//     }
// }
