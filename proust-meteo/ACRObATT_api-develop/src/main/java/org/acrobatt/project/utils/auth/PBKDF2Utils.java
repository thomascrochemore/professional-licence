package org.acrobatt.project.utils.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class PBKDF2Utils {

    private static Logger logger = LogManager.getLogger(PBKDF2Utils.class);

    /**
     * The number of bytes of the generated salt
     */
    private static final int SALT_BYTES = 32;

    /**
     * THe number of bytes for the hash
     */
    private static final int HASH_BYTES = 32;

    /**
     * The number of hashing iterations
     */
    private static final int ITERATIONS = 16;

    private PBKDF2Utils() {}

    /**
     * Verifies if the given password corresponds to the hashed password in the database
     * @param password the plain-text password
     * @param hash the hash
     * @return true if the hash corresponds to the plain-text password, false if not
     * @throws IOException if something happened during decoding
     * @throws NullPointerException if one of the parameters is null
     */
    public static boolean verifyPassword(String password, String hash) throws IOException, NullPointerException {
        String[] params = hash.split(":");
        byte[] salt = Base64.getDecoder().decode(params[0]);

        String h = hashPassword(password, salt);
        if(h == null) throw new NullPointerException();
        return h.equals(hash);
    }

    /**
     * Hashes the given plain-text password and returns the hash
     * @param pass the plain-text password
     * @return the hashed password with the salt ("salt:hash")
     */

    public static String hashPassword(String pass) {
        try {
            byte[] salt = generateSalt();

            //salt and hash
            byte[] hash = generatePBKDF2Secret(pass.toCharArray(), salt);
            String ehash = Base64.getEncoder().encodeToString(hash);
            String esalt = Base64.getEncoder().encodeToString(salt);
            logger.debug("hash = [" + esalt + ":" + ehash + "]");

            return (esalt + ":" + ehash);
        } catch(NoSuchAlgorithmException | InvalidKeySpecException e) {
            return null;
        }
    }

    /**
     * Hashes the given plain-text password using the given salt and returns the hash ("salt:hash")
     * @param pass the plain-text password
     * @param salt the salt
     * @return the hash
     */
    private static String hashPassword(String pass, byte[] salt) {
        try {

            //salt and hash
            byte[] hash = generatePBKDF2Secret(pass.toCharArray(), salt);
            String ehash = Base64.getEncoder().encodeToString(hash);
            String esalt = Base64.getEncoder().encodeToString(salt);
            logger.debug("hash = [" + esalt + ":" + ehash + "]");

            return (esalt + ":" + ehash);
        } catch(NoSuchAlgorithmException | InvalidKeySpecException e) {
            return null;
        }
    }

    /**
     * Generates a salt for hashing
     * @return the salt
     */
    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_BYTES];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(salt);
        return salt;
    }

    /**
     * generates the PBKDF2 secret
     * @param pass the plain-text password
     * @param salt the salt
     * @return a hashed password
     * @throws NoSuchAlgorithmException if the supplied algorithm doesn't exist
     * @throws InvalidKeySpecException if the key spec doesn't conform to the algorithm
     */
    private static byte[] generatePBKDF2Secret(char[] pass, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(pass, salt, ITERATIONS, HASH_BYTES * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return skf.generateSecret(spec).getEncoded();
    }

    /**
     * Gets the salt in a hash
     * @param hash the hash
     * @return the salt
     * @throws IOException if decoding fails
     */
    public static byte[] getSalt(String hash) {
        return Base64.getDecoder().decode(hash.split(":")[0]);
    }

    /**
     * Gets the hashed password in a hash
     * @param hash the hash
     * @return the hashed password
     * @throws IOException if decoding fails
     */
    public static byte[] getPass(String hash) {
        return Base64.getDecoder().decode(hash.split(":")[1]);
    }
}
