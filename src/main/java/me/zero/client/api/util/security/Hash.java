package me.zero.client.api.util.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to hash data with a defined algorithm
 *
 * @author Brady
 * @since 2/25/2017 12:00 PM
 */
public final class Hash {

    /**
     * Cache of all Hash instances
     */
    private static final Map<String, Hash> HASH_CACHE = new HashMap<>();

    /**
     * Algorithm of this Hash object
     */
    private final String algorithm;

    private Hash(String algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Creates a new Hash object with the specified algorithm
     *
     * @param algorithm Algorithm name
     * @return Hash object
     */
    public static Hash of(String algorithm) {
        return HASH_CACHE.computeIfAbsent(algorithm, Hash::new);
    }

    /**
     * Hashes a piece of data with the algorithm
     *
     * @param data Data being hashed
     * @return Hashed data
     */
    public final String hash(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes)
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            return sb.toString();
        } catch (NoSuchAlgorithmException e2) {
            return data;
        }
    }
}
