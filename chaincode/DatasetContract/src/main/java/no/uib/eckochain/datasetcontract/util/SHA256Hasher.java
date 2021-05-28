/* SPDX-License-Identifier: Apache-2.0 */
package main.java.no.uib.eckochain.datasetcontract.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class for creating SHA-256 hash
 */
public class SHA256Hasher {
    public static String getHash(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] mdBytes = md.digest(string.getBytes(StandardCharsets.UTF_8));
            BigInteger num = new BigInteger(1, mdBytes);
            StringBuilder hash = new StringBuilder(num.toString(16));
            while (hash.length() < 32) {
                hash.insert(0, "0");
            }
            return hash.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
