package UTCN_IMDB.demo.utils;

import java.util.Date;

public class ForgottenPasswordUtils {

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    public static Date calculateExpirationDate(int minutes) {
        long currentTime = System.currentTimeMillis();
        long expirationTime = currentTime + (minutes * 60 * 1000);
        return new Date(expirationTime);
    }
}
