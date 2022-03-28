package com.urlshortener.urlshortener.controller

import com.urlshortener.urlshortener.service.UrlService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.HashMap

@RestController
@RequestMapping(path = ["api/v1/url"])
class UrlController(private val urlService: UrlService) {

    private var logger = LoggerFactory.getLogger(javaClass)

    /**
     * HTTP POST request for getting short url id for the long url id passed in request body
     *
     * sample endpoint: http://localhost:8080/api/v1/url/
     *
     * sample request body:
     * {
     * "url": "http://google.com"
     * }
     *
     * sample response body:
     * {
     * "id": "1L9zO9O"
     * }
     *
     * @param body
     * @return short url id corresponding to the long url
     */
    @PostMapping(path = [""])
    fun createShortUrlFromLongUrl(@RequestBody body: Map<String?, String?>): Map<String, String?> {
        val shortId = body["url"]?.let { urlService.createShortUrlFromLongUrl(it) }
        val response: MutableMap<String, String?> = HashMap()
        response["id"] = shortId
        return response // url is the long url for which will get short url
    }

    /**
     * HTTP GET request for fetching long url corresponding to the short ur id passed as path variable
     *
     * sample endpoint: http://localhost:8080/api/v1/url/1L9zO9O
     *
     * sample response body:
     * {
     * "url": "http://google.com"
     * }
     *
     * @param shortUrl shortUrl passed as path variable in endpoint
     * @return short url id corresponding to the long url
     */
    @GetMapping(path = ["/{shortUrl}"])
    fun getLongUrlFromShortUrl(@PathVariable("shortUrl") shortUrl: String): Map<String, String?> {
        val response: MutableMap<String, String?> = HashMap()
        val url = urlService.getLongUrlFromShortUrl(shortUrl)
        response["url"] = url
        return response
    }

    /*@PutMapping(path = "/{urlid}")
    public Map<String,String> changeID(@RequestBody Map<String,String> id,@PathVariable("urlid")String urlid){
        //urlid is the id which is recently stored as a hash for the long url and id is the new id we want to replace
        // the recent one with
        return urlService.changeId(urlid,id.get("id"));
    }*/
}
