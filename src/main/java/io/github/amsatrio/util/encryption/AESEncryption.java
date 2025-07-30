package io.github.amsatrio.util.encryption;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.enterprise.context.ApplicationScoped;

import io.github.amsatrio.annotation.AESQualifier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AESQualifier
@ApplicationScoped
public class AESEncryption implements EncryptionProvider {
    private SecretKey secretKey = null;
    private GCMParameterSpec iv = null;
    private String algorithm = "AES/GCM/NoPadding";
    private Cipher decryptCipher = null;
    private Cipher encryptCipher = null;

    @PostConstruct
    private void init() {
        try {
            secretKey = generateKey(128);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return;
        }
        iv = generateIv();

        try {
            decryptCipher = Cipher.getInstance(algorithm);
            decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

            encryptCipher = Cipher.getInstance(algorithm);
            encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    private SecretKey generateKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    private GCMParameterSpec generateIv() {
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        return new GCMParameterSpec(128, iv);
    }

    @Override
    public byte[] decrypt(byte[] input) {
        byte[] plainText = null;
        try {
            plainText = decryptCipher.doFinal(Base64.getDecoder()
                    .decode(input));
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return plainText;
    }

    @Override
    public byte[] encrypt(byte[] input) {
        byte[] cipherText = null;
        try {
            cipherText = encryptCipher.doFinal(input);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encode(cipherText);
    }

}
