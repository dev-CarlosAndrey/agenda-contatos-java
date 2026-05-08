package util;

import java.util.regex.Pattern;

public class Validation {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PHONE_REGEX = "^\\d{10,11}$";

    public static boolean isValidEmail(String email){
        return email != null && Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean isValidPhone(String phone) {
        String cleanPhone = phone.replaceAll("[\\s()+-]", "");
        return Pattern.matches(PHONE_REGEX, cleanPhone);
    }
}
