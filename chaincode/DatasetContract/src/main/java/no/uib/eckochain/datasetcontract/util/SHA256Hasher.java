/* SPDX-License-Identifier: Apache-2.0 */
package main.java.no.uib.eckochain.datasetcontract.util;

import org.apache.commons.codec.binary.Hex;

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
            return Hex.encodeHexString(mdBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
