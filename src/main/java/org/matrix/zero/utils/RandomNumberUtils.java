package org.matrix.zero.utils;

import java.util.Random;

public class RandomNumberUtils {

    private RandomNumberUtils() {}

    private static Random rand = new Random();

    private static final int min = 3;

    private static final int max = 5;

    public static int generateRandomNumberBetween() {
        return rand.nextInt((max - min) + 1) + min;
    }
}
