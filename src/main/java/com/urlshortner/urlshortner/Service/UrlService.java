package com.urlshortner.urlshortner.Service;

import org.springframework.stereotype.Service;

@Service
public interface UrlService {

    String createShortUrlFromLongUrl(String longUrl);

    String getLongUrlFromShortUrl(String shortUrl);

}
