"use client";
import React, { useState } from "react";
import { FaCopy } from "react-icons/fa";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Header from "../components/header"; // Assuming you have a Header component for your page layout.

const OTPPage = () => {
    const [message, setMessage] = useState(""); // For the message to encrypt
    const [encryptedMessage, setEncryptedMessage] = useState(""); // For showing encrypted message
    const [encryptionKey, setEncryptionKey] = useState(""); // For the encryption key
    const [decryptedMessage, setDecryptedMessage] = useState(""); // For the decrypted message input (encrypted text)
    const [decryptionKey, setDecryptionKey] = useState(""); // For the decryption key
    const [plainText, setPlainText] = useState(""); // For the decrypted plain text output
    const handleEncrypt = async () => {
        try {
            // Ensure both the message (plainText) and key are encoded in UTF-8
            const utf8Message = new TextEncoder().encode(message);
            const utf8Key = new TextEncoder().encode(encryptionKey);

            const response = await fetch("http://localhost:8080/api/otp/encrypt", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    plainText: new TextDecoder().decode(utf8Message),
                    key: new TextDecoder().decode(utf8Key),
                }),
            });

            if (response.ok) {
                const data = await response.json();
                setEncryptedMessage(data.cipherText);  // Base64-encoded cipher text
                setEncryptionKey(data.key);
            } else {
                console.error("Failed to encrypt message");
                toast.error("Encryption failed");
            }
        } catch (error) {
            console.error("Error encrypting message:", error);
            toast.error("Error encrypting message");
        }
    };

    const handleDecrypt = async () => {
        try {
            // Send the Base64 encoded string directly to the backend
            const response = await fetch("http://localhost:8080/api/otp/decrypt", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    cipherText: decryptedMessage,  // Send the Base64 string directly
                    key: decryptionKey,  // Send the decryption key as a string
                }),
            });
    
            if (response.ok) {
                const decrypted = await response.text();
                setPlainText(decrypted);  // Set decrypted result to plainText state
            } else {
                console.error("Failed to decrypt message");
                toast.error("Decryption failed");
            }
        } catch (error) {
            console.error("Error decrypting message:", error);
            toast.error("Error decrypting message");
        }
    };
    
    // Copy Text to Clipboard
    const copyToClipboard = (text: string) => {
        navigator.clipboard.writeText(text).then(() => {
            toast.success("Copied to clipboard!");
        }).catch((error) => {
            console.error("Failed to copy text:", error);
            toast.error("Failed to copy text");
        });
    };

    return (
        <>
            <Header />
            <div className="otp-container">
                <h1 className="text-4xl font-bold text-center text-gray-200 drop-shadow-lg">
                    OTP Encryption/Decryption
                </h1>

                <div className="otp-section">
                    {/* Encryption Container */}
                    <div className="otp-card">
                        <h2 className="text-2xl font-semibold text-center text-gray-200 mb-4">Encrypt Message</h2>

                        <textarea
                            value={message}
                            onChange={(e) => setMessage(e.target.value)}
                            className="otp-textarea"
                            placeholder="Enter secret message here"
                        ></textarea>

                        <button
                            onClick={handleEncrypt}
                            className="otp-button"
                        >
                            Encrypt
                        </button>

                        {encryptedMessage && (
                            <div className="otp-result">
                                <p className="text-gray-200">Encrypted Message:</p>
                                <textarea
                                    value={encryptedMessage}
                                    readOnly
                                    className="otp-message-textarea"
                                    onFocus={(e) => e.target.select()} // Ensure full selection when focused
                                ></textarea>

                                <div className="otp-copy-buttons">
                                    <button
                                        onClick={() => copyToClipboard(encryptedMessage)}
                                        className="otp-copy-button"
                                    >
                                        <FaCopy /> Copy Encrypted Text
                                    </button>

                                    <button
                                        onClick={() => copyToClipboard(encryptionKey)}
                                        className="otp-copy-button"
                                    >
                                        <FaCopy /> Copy Key
                                    </button>
                                </div>
                            </div>
                        )}
                    </div>

                    {/* Decryption Container */}
                    <div className="otp-card">
                        <h2 className="text-2xl font-semibold text-center text-gray-200 mb-4">Decrypt Message</h2>

                        <textarea
                            value={decryptedMessage}
                            onChange={(e) => setDecryptedMessage(e.target.value)}
                            className="otp-textarea"
                            placeholder="Enter encrypted message here"
                        />

                        <input
                            type="text"
                            value={decryptionKey}
                            onChange={(e) => setDecryptionKey(e.target.value)}
                            className="otp-textarea"
                            placeholder="Enter decryption key"
                        />

                        <button
                            onClick={handleDecrypt}
                            className="otp-button"
                        >
                            Decrypt
                        </button>

                        {plainText && (
                            <div className="otp-result">
                                <p className="text-gray-200">Decrypted Message:</p>
                                <textarea
                                    value={plainText}
                                    readOnly
                                    className="otp-message-textarea"
                                    onFocus={(e) => e.target.select()} // Ensure full selection when focused
                                ></textarea>

                                <button
                                    onClick={() => copyToClipboard(plainText)}
                                    className="otp-button"
                                >
                                    <FaCopy /> Copy Decrypted Text
                                </button>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </>
    );
};

export default OTPPage;
