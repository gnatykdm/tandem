package com.tandem.utils;

import java.security.SecureRandom;

public class GenKey {
    private final SecureRandom random = new SecureRandom();

    public int genKey() {
        return random.nextInt(90000) + 10000;
    }
}
