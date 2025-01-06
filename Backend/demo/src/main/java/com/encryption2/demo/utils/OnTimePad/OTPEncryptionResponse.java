package com.encryption2.demo.utils.OnTimePad;

public class OTPEncryptionResponse {
    private String cipherText;
    private String key;

    public OTPEncryptionResponse(String cipherText, String key) {
        this.cipherText = cipherText;
        this.key = key;
    }

    // Getter and Setter methods
    public String getCipherText() {
        return cipherText;
    }

    public void setCipherText(String cipherText) {
        this.cipherText = cipherText;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
