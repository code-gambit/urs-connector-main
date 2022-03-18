package com.urlshortner.urlshortner.Controller;

import com.urlshortner.urlshortner.Service.UrlService;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/url")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService){
        this.urlService=urlService;
    }

    /**
     * HTTP POST request for getting short url id for the long url id passed in request body
     *
     * sample endpoint: http://localhost:8080/api/v1/url/
     *
     * sample request body:
     *  {
     *      "url": "http://google.com"
     *  }
     *
     * sample response body:
     *  {
     *      "id": "1L9zO9O"
     *  }
     *
     * @param body
     * @return short url id corresponding to the long url
     */
    @PostMapping(path = "")
    public Map<String,String> createShortUrlFromLongUrl(@RequestBody Map<String,String> body){
        String shortId = urlService.createShortUrlFromLongUrl(body.get("url"));
        Map<String, String> response = new HashMap<>();
        response.put("id", shortId);
        return response; //url is the long url for which will get short url
    }

    /**
     * HTTP GET request for fetching long url corresponding to the short ur id passed as path variable
     *
     * sample endpoint: http://localhost:8080/api/v1/url/1L9zO9O
     *
     * sample response body:
     *  {
     *      "url": "http://google.com"
     *  }
     *
     * @param shortUrl shortUrl passed as path variable in endpoint
     * @return short url id corresponding to the long url
     */
    @GetMapping(path = "/{shortUrl}")
    public Map<String, String> getLongUrlFromShortUrl(@PathVariable("shortUrl") String shortUrl){
        Map<String, String> response = new HashMap<>();
        String url = urlService.getLongUrlFromShortUrl(shortUrl);
        response.put("url", url);
        return response;
    }

    /*@PutMapping(path = "/{urlid}")
    public Map<String,String> changeID(@RequestBody Map<String,String> id,@PathVariable("urlid")String urlid){
        //urlid is the id which is recently stored as a hash for the long url and id is the new id we want to replace
        // the recent one with
        return urlService.changeId(urlid,id.get("id"));
    }*/

}
