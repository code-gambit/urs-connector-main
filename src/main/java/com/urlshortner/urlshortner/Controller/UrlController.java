package com.urlshortner.urlshortner.Controller;
import com.urlshortner.urlshortner.Service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import java.util.Map;

@RestController
@RequestMapping(path = "URS/api/test/v1/url")
public class UrlController {

    private final UrlService urlService;
    @Autowired
    public UrlController(UrlService urlService){
        this.urlService=urlService;
    }

    @PostMapping(path = "")
    public Map<String,String> createShortUrlFromLongUrl(@RequestBody Map<String,String> url){
        return urlService.createShortUrlFromLongUrl(url.get("url"));//url is the long url for which will get short url
    }

    @GetMapping(path = "/{shortUrl}")
    public RedirectView getLongUrlFromShortUrl(@PathVariable("shortUrl")String shortUrl){
        Map<String,String> result=urlService.getLongUrlFromShortUrl(shortUrl);
        RedirectView redirectView=new RedirectView();
        String url=result.get("url");
        if(!url.equals("URL not available")) {
            redirectView.setUrl(url);
            return redirectView;//It will redirect the user to mapped long url of the hash he passes.
        }
        else {
            return null;
        }
    }

    @PutMapping(path = "/{urlid}")
    public Map<String,String> changeID(@RequestBody Map<String,String> id,@PathVariable("urlid")String urlid){
        //urlid is the id which is recently stored as a hash for the long url and id is the new id we want to replace
        // the recent one with
        return urlService.changeId(urlid,id.get("id"));
    }

}
