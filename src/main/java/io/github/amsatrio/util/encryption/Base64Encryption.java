package io.github.amsatrio.util.encryption;

import java.util.Base64;

import javax.enterprise.context.ApplicationScoped;

import io.github.amsatrio.annotation.Base64Qualifier;

@Base64Qualifier
@ApplicationScoped
public class Base64Encryption implements EncryptionProvider {

    @Override
    public byte[] decrypt(byte[] input) {
        return Base64.getDecoder().decode(input);
    }

    @Override
    public byte[] encrypt(byte[] input) {
        return Base64.getEncoder().encode(input);
    }
    
}
