package org.acrobatt.project.utils.auth;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;

public class JWTAuthKeyGenerator {
    /**
     * Signing key
     */
    private static Key key;

    private JWTAuthKeyGenerator() {}

    /**
     * Gets the currently stored key or generates it if it doesn't exist
     * @return the signing key used for JWTs
     */
    public static Key getKey() {
        if(key == null) {
            key = MacProvider.generateKey(SignatureAlgorithm.HS256);
        }
        return key;
    }

    /**
     * Regenerates the key and overwrites the previous one, invalidating any JWT using the previous key
     * @return the new signing key
     */
    public static Key regenerateKey() {
        key = MacProvider.generateKey(SignatureAlgorithm.HS256);
        return key;
    }
}
