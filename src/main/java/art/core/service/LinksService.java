package art.core.service;

import art.core.model.User;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class LinksService {
    private static final String CHARSET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = CHARSET.length(); // 62
    public static String encode(long num) {
        if (num == 0) return "0";

        StringBuilder sb = new StringBuilder();

        while (num > 0) {
            int index = (int)(num % BASE);
            sb.append(CHARSET.charAt(index));
            num /= BASE;
        }

        return sb.reverse().toString();
    }

    public static long decode(String str) {
        long result = 0;

        for (int i = 0; i < str.length(); i++) {
            result = result * BASE + CHARSET.indexOf(str.charAt(i));
        }

        return result;
    }
}
