package com.encryption2.demo.utils.OnTimePad;

public class OTPEncryptionRequest {
    private String plainText;
    private String cipherText;
    private String key;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
