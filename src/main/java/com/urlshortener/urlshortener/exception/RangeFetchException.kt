package com.urlshortener.urlshortener.exception

import java.lang.Exception

class RangeFetchException : Exception() {
    override val message: String
        get() = "Not able to fetch range from zookeeper"

    override fun getLocalizedMessage(): String {
        return message
    }
}
