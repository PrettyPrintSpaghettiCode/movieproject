package com.thalesgroup.restlib.util;


import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;


/**
 * Util class for secure API key storage
 */
public class ApiKeyHandler {

    private static final String KEYSTORE_ALIAS = "API_KEY_SECRET_KEY";
    private static final String KEYSTORE_PROVIDER = "AndroidKeyStore";
    private static final String CIPHER_ALGO = "AES/CBC/PKCS7Padding";
    private static final String ENCRYPT_FILENAME = "keyfile.txt";
    private static final int CIPHER_IV_LENGTH = 16;
    private static KeyStore keyStore = null;


    /**
     * Load the Keystore
     */
    private static void loadKeyStore() {
        if (keyStore == null) {
            try {
                keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
                keyStore.load(null);
            } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Generate a secret key from KeyGenerator
     * that is stored in Android Keystore System
     * @return secret key
     */
    private static SecretKey generateSecretKey() {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,
                    KEYSTORE_PROVIDER);
            keygen.init(new KeyGenParameterSpec.Builder(KEYSTORE_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            return keygen.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get secret key from Keystore
     * @return secret key
     */
    private static SecretKey getSecretKey() throws UnrecoverableKeyException {
        loadKeyStore();
        SecretKey secretKey = null;
        try {
            // get secret key from keystore
            if ((keyStore.containsAlias(KEYSTORE_ALIAS)) &&
                    (secretKey = (SecretKey) keyStore.getKey(KEYSTORE_ALIAS, null)) != null) {
                return secretKey;
            }
        } catch (KeyStoreException | UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // if not found, generate a new one
        if((secretKey = generateSecretKey()) != null) {
            return secretKey;
        }

        // if no key is generated, throw exception
        throw new UnrecoverableKeyException();
    }

    /**
     * Encrypt API key
     * @param apiKey API key in clear
     * @return IV + ciphered API key
     */
    private static byte[] encryptAPIKey(String apiKey) throws UnrecoverableKeyException {
        byte[] cipherText, iv, cipherTextChk = null;
        byte[] plainText = apiKey.getBytes();

        try {
            // encrypt
            Cipher cipher  = Cipher.getInstance(CIPHER_ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
            cipherText = cipher.doFinal(plainText);
            // get IV
            iv = cipher.getIV();
            if(iv.length != CIPHER_IV_LENGTH) {
                throw new AssertionError("Unexpected IV length");
            }
            // concat IV + cipher text
            cipherTextChk = new byte[cipherText.length + iv.length];
            System.arraycopy(iv, 0, cipherTextChk, 0, iv.length);
            System.arraycopy(cipherText, 0, cipherTextChk, iv.length, cipherText.length);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            try {
                keyStore.deleteEntry(KEYSTORE_ALIAS);
            } catch (KeyStoreException ex) {
                ex.printStackTrace();
            }
        } catch (NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | AssertionError e) {
            e.printStackTrace();
        }
        return cipherTextChk;
    }

    /**
     * Decrypt API key
     * @param cipherTextChk IV + ciphered API key
     * @return API key in clear
     */
    private static String decryptAPIKey(byte[] cipherTextChk) throws UnrecoverableKeyException {
        byte[] clearText, cipherText, iv = null;
        int cipherLen = cipherTextChk.length - CIPHER_IV_LENGTH;

        try {
            // prepare cipher text and iv buffer
            cipherText = new byte[cipherLen];
            System.arraycopy(cipherTextChk, CIPHER_IV_LENGTH, cipherText, 0, cipherLen);
            iv = new byte[CIPHER_IV_LENGTH];
            System.arraycopy(cipherTextChk, 0, iv, 0, CIPHER_IV_LENGTH);
            // decrypt
            Cipher cipher  = Cipher.getInstance(CIPHER_ALGO);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), new IvParameterSpec(iv));
            clearText = cipher.doFinal(cipherText);
            return new String(clearText);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        // if API key cannot be decrypted, throw exception
        throw new UnrecoverableKeyException();
    }

    /**
     * Save API key in file, value encrypted
     * @param context caller context
     * @param apiKey API key in clear
     */
    public static void saveAPIKey(Context context, String apiKey) throws UnrecoverableKeyException {
        // encrypt API key
        byte[] cipheredAPIKey = encryptAPIKey(apiKey);
        try {
            // file path to store API key
            String filepath = context.getFilesDir().getAbsolutePath() + "/" + ENCRYPT_FILENAME;
            File file = new File(filepath);
            // write encrypted API key to file
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(cipheredAPIKey);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve API key from file
     * @param context caller context
     * @return API key in clear
     */
    public static String retrieveAPIkey(Context context) throws UnrecoverableKeyException, IOException {
        FileInputStream fileInputStream = null;

        // file path to retrieve API key
        String filepath = context.getFilesDir().getAbsolutePath() + "/" + ENCRYPT_FILENAME;
        File file = new File(filepath);
        // prepare byte array buffer
        byte cipheredTextChk[] = new byte[(int)file.length()];
        // read file content
        fileInputStream = new FileInputStream(file);
        fileInputStream.read(cipheredTextChk);

        // decrypt API key
        return decryptAPIKey(cipheredTextChk);
    }

}
