package com.urlshortner.urlshortner.Service;

import com.urlshortner.urlshortner.Model.UrlModel;
import com.urlshortner.urlshortner.Repository.UrlRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UrlServiceImpl implements UrlService {
    private static long counter = 100000000000L;

    private final UrlRepository urlRepository;

    public UrlServiceImpl(UrlRepository urlRepository){
        this.urlRepository=urlRepository;
    }

    /**
     * Generates the short url id and stores in db
     * @param longUrl
     * @return short url id corresponding to the [longUrl] passed
     */
    public String createShortUrlFromLongUrl(String longUrl){
        String shortUrlId;
        List<UrlModel> Url=urlRepository.findByLongUrl(longUrl);
        if(Url.size()<1) {
            shortUrlId = "1L9zO9O"; // TODO call the bse62 convertor here
            UrlModel urlModel =  new UrlModel.Builder()
                                            .shortUrl(shortUrlId)
                                            .longUrl(longUrl).build();
            urlRepository.save(urlModel);
        } else {
            shortUrlId=Url.get(0).getShortUrl();
        }
        return shortUrlId;
    }

    /**
     * Fetches the [longUrl] from db and returns it
     * @param shortUrl
     * @return long url corresponding to the [shortUrl] passed
     */
    public String getLongUrlFromShortUrl(String shortUrl){
        String longUrl;
        List<UrlModel> urlModelList = urlRepository.findByShortUrl(shortUrl);
        if(urlModelList.size()==0) {
            longUrl = null;
        } else {
            longUrl = urlModelList.get(0).getLongUrl();
        }
        return longUrl;
    }

    /*public Map<String,String> changeId(String id,String url) {
        Map<String,String> res=new HashMap<>();
        //Check if the shortUrl provided exists or not
        int count=urlRepository.countUrlModelByShortUrl(id);
        //Check if the new url provided already exists or not
        int countNew=urlRepository.countUrlModelByShortUrl(url);
        String newUrl;
        UrlModel urlModel = urlRepository.findUrlModelByShortUrl(id);
        if(count>0){
            if(countNew==0){
                urlModel.setShortUrl(url);
                urlRepository.save(urlModel);
                newUrl = "http://localhost:8085/URS/api/test/v1/url/" + url;
            }else{
                newUrl="new id provided already exists";
            }
        }
        else{
           newUrl="id not found";
        }
        res.put("url", newUrl);
        return res;
    }*/
}
