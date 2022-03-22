package com.urlshortner.urlshortner.utility

import java.util.ArrayList

object ExtensionFunction {
    var ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    var BASE = ALPHABET.length

    /**
     * Encodes the given [counter] long value to base62 string
     *
     * limitation:
     * since this algorithm expects long so the range of possible values are
     * -2^63 to 2^63 - 1
     *
     * @param counter
     * @return BASE62 encoded string of [counter]
     */
    private fun base62Encode(counter: Long): String {
        var counter = counter
        if (counter == 0L) {
            return ALPHABET[0].toString()
        }
        val arr: MutableList<String?> = ArrayList()
        while (counter > 0) {
            val rem = Math.toIntExact(counter % BASE)
            counter = Math.ceil((counter / BASE).toDouble()).toLong()
            arr.add(ALPHABET[rem].toString())
        }
        arr.reverse()
        return java.lang.String.join("", arr)
    }

    fun Long.toBase62(): String {
        return base62Encode(this)
    }
}
