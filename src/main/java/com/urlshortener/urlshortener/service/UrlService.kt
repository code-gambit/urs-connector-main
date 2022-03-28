package com.urlshortener.urlshortener.service

import org.springframework.stereotype.Service

@Service
interface UrlService {
    fun createShortUrlFromLongUrl(longUrl: String): String?
    fun getLongUrlFromShortUrl(shortUrl: String): String?
}
