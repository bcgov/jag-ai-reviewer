package ca.bc.gov.open.jag.aireviewerapi.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

    private MD5() {}

    public static String hashValue(String value) {

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] bytes = md.digest(value.getBytes());

            StringBuilder sb = new StringBuilder();

            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            return "";
        }

    }

}
