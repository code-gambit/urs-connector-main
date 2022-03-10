package com.urlshortner.urlshortner.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UrlUtils {

    public static String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static int BASE = ALPHABET.length();

    /**
     * Encodes the given [counter] long value to base62 string
     *
     * limitation:
     *  since this algorithm expects long so the range of possible values are
     *  -2^63 to 2^63 - 1
     *
     * @param counter
     * @return BASE62 encoded string of [counter]
     */
    public static String base62Encode(long counter) {
        if (counter == 0) {
            return String.valueOf(ALPHABET.charAt(0));
        }
        List<String> arr = new ArrayList<>();
        while (counter > 0) {
            int rem = Math.toIntExact(counter % BASE);
            System.out.println("Rem: " + rem);
            counter = (long) Math.ceil(counter / BASE);
            arr.add(String.valueOf(ALPHABET.charAt(rem)));
        }
        Collections.reverse(arr);
        return String.join("", arr);
    }

}
