package io.github.amsatrio.util.encryption;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface EncryptionProvider {
    byte[] decrypt(byte[] input);
    byte[] encrypt(byte[] input);
}
