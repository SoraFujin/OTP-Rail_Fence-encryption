package com.encryption2.demo.utils.RailFence;

public class RailFenceEncryptionRequest {
    private String plainText;
    private String cipherText;
    private int key;

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
}
