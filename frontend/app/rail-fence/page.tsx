"use client";
import React, { useState } from "react";
import axios from "axios";
import Header from "../components/header";
import { FaCopy } from "react-icons/fa";

const RailFence = () => {
    const [message, setMessage] = useState("");
    const [encryptedMessage, setEncryptedMessage] = useState("");
    const [railsEncryption, setRailsEncryption] = useState("");
    const [cipherText, setCipherText] = useState("");
    const [railsDecryption, setRailsDecryption] = useState("");
    const [decryptedText, setDecryptedText] = useState("");

    const handleEncrypt = async () => {
        try {
            if (parseInt(railsEncryption) < 2) {
                alert("Number of rails must be greater than 1");
                return;
            }

            const response = await axios.post("http://localhost:8080/api/railfence/encrypt", {
                plainText: message,
                key: parseInt(railsEncryption)
            });

            const data = response.data;
            if (data === "Key must be greater than 1") {
                alert(data);
            } else {
                setEncryptedMessage(data);
            }
        } catch (error) {
            console.error("Error during encryption", error);
            alert("Error during encryption. Please check your input or server.");
        }
    };

    const handleDecrypt = async () => {
        try {
            if (parseInt(railsDecryption) < 2) {
                alert("Number of rails must be greater than 1");
                return;
            }

            const response = await axios.post("http://localhost:8080/api/railfence/decrypt", {
                cipherText,
                key: parseInt(railsDecryption)
            });

            const data = response.data;
            if (data === "Key must be greater than 1") {
                alert(data);
            } else {
                setDecryptedText(data);
            }
        } catch (error) {
            console.error("Error during decryption", error);
            alert("Error during decryption. Please check your input or server.");
        }
    };

    const copyToClipboard = (text: string) => {
        navigator.clipboard.writeText(text).then(() => {
            alert("Copied to clipboard!");
        });
    };

    return (
        <>
            <Header />
            <div className="railfence-container bg-gray-950 p-8 min-h-screen">
                <h1 className="text-4xl font-bold text-center text-gray-200 drop-shadow-lg">Rail Fence Encryption & Decryption</h1>

                {/* Encryption and Decryption Section */}
                <div className="otp-section flex gap-6 mt-12 justify-center">
                    {/* Encryption Container */}
                    <div className="otp-card bg-gray-800 p-8 rounded-lg shadow-lg w-full max-w-lg flex-grow">
                        <h2 className="text-2xl font-semibold text-center text-gray-200 mb-4">Encrypt</h2>
                        <textarea
                            className="otp-textarea w-full p-4 mt-4 rounded-lg text-gray-800 focus:outline-none focus:ring-2 focus:ring-indigo-400 border-gray-700 shadow-md"
                            value={message}
                            onChange={(e) => setMessage(e.target.value)}
                            placeholder="Enter text to encrypt"
                        />
                        <input
                            type="number"
                            value={railsEncryption}
                            onChange={(e) => setRailsEncryption(e.target.value)}
                            className="mt-4 w-full p-4 rounded-lg text-gray-800 border-gray-700 shadow-md focus:ring-2 focus:ring-indigo-400"
                            placeholder="Number of rails"
                        />
                        <button onClick={handleEncrypt} className="otp-button mt-4 bg-indigo-500 text-white px-6 py-2 rounded-lg hover:bg-indigo-400 transition-all duration-300 focus:outline-none">
                            Encrypt
                        </button>

                        {encryptedMessage && (
                            <div className="otp-result mt-6 text-center">
                                <p>Encrypted Text:</p>
                                <textarea className="otp-textarea w-full p-4 mt-4 rounded-lg text-gray-800 focus:outline-none border-gray-700 shadow-md" value={encryptedMessage} readOnly />
                                <div className="otp-copy-buttons flex justify-between gap-4 mt-4">
                                    <button onClick={() => copyToClipboard(encryptedMessage)} className="otp-copy-button bg-indigo-500 text-white px-6 py-2 rounded-lg hover:bg-indigo-400 transition-all duration-300">
                                        <FaCopy /> Copy Encrypted Text
                                    </button>
                                </div>
                            </div>
                        )}
                    </div>

                    {/* Decryption Container */}
                    <div className="otp-card bg-gray-800 p-8 rounded-lg shadow-lg w-full max-w-lg flex-grow">
                        <h2 className="text-2xl font-semibold text-center text-gray-200 mb-4">Decrypt</h2>
                        <textarea
                            className="otp-textarea w-full p-4 mt-4 rounded-lg text-gray-800 focus:outline-none focus:ring-2 focus:ring-indigo-400 border-gray-700 shadow-md"
                            value={cipherText}
                            onChange={(e) => setCipherText(e.target.value)}
                            placeholder="Enter text to decrypt"
                        />
                        <input
                            type="number"
                            value={railsDecryption}
                            onChange={(e) => setRailsDecryption(e.target.value)}
                            className="mt-4 w-full p-4 rounded-lg text-gray-800 border-gray-700 shadow-md focus:ring-2 focus:ring-indigo-400"
                            placeholder="Number of rails"
                        />
                        <button onClick={handleDecrypt} className="otp-button mt-4 bg-indigo-500 text-white px-6 py-2 rounded-lg hover:bg-indigo-400 transition-all duration-300 focus:outline-none">
                            Decrypt
                        </button>

                        {decryptedText && (
                            <div className="otp-result mt-6 text-center">
                                <p>Decrypted Text:</p>
                                <textarea className="otp-textarea w-full p-4 mt-4 rounded-lg text-gray-800 focus:outline-none border-gray-700 shadow-md" value={decryptedText} readOnly />
                                <div className="otp-copy-buttons flex justify-between gap-4 mt-4">
                                    <button onClick={() => copyToClipboard(decryptedText)} className="otp-copy-button bg-indigo-500 text-white px-6 py-2 rounded-lg hover:bg-indigo-400 transition-all duration-300">
                                        <FaCopy /> Copy Decrypted Text
                                    </button>
                                </div>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </>
    );
};

export default RailFence;
