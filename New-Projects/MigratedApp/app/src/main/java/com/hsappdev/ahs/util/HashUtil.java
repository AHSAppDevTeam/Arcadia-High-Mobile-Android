package com.hsappdev.ahs.util;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    /**
     * hashes data
     * @param data - data in serialized message pack format
     * @param salt - the password basically
     * @return a hashed version that is sent through nfc
     */
    public static byte[] getSha256Hash(byte[] data, byte[] salt) {
        try {
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("SHA-512");
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
            outputStream.write(data);
            outputStream.write(salt);
            return digest.digest(outputStream.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

}
