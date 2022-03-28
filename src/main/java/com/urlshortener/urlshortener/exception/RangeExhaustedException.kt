package com.urlshortener.urlshortener.exception

import java.lang.Exception

class RangeExhaustedException : Exception() {
    override val message: String
        get() = "Range exhausted"

    override fun getLocalizedMessage(): String {
        return message
    }
}
