package org.starter.springboot.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TokenUtils {

    public static String generateToken() {
        StringBuilder sb = new StringBuilder();
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        byte[] bytes = md.digest();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
