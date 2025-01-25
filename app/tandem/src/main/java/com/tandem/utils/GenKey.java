package com.tandem.utils;

import java.security.SecureRandom;

public class GenKey {

    private final String LOWERCASE = "qwertyuiopasdfghjklzxcvbnm";
    private final String UPPER = LOWERCASE.toUpperCase();
    private final String NUMS = "1234567890";
    private final String ALL = LOWERCASE + UPPER + NUMS;

    private final SecureRandom random = new SecureRandom();

    public int genKey() {
        return random.nextInt(90000) + 10000;
    }

    public String generateGroupKey() {
        int groupKeyLength = 10;

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < groupKeyLength; i++) {
            builder.append(ALL.charAt(random.nextInt(0, ALL.length())));
        }
        return builder.toString();
    }
}
