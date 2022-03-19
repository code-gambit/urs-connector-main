package com.urlshortner.urlshortner.service

import com.urlshortner.urlshortner.repository.UrlRepository
import com.urlshortner.urlshortner.model.UrlModel
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UrlServiceImpl(private val urlRepository: UrlRepository) : UrlService {

    private var logger = LoggerFactory.getLogger(javaClass)

    /**
     * Generates the short url id and stores in db
     * @param longUrl
     * @return short url id corresponding to the [longUrl] passed
     */
    override fun createShortUrlFromLongUrl(longUrl: String): String? {
        val shortUrlId: String
        val url = urlRepository.findByLongUrl(longUrl)
        if (url!!.isEmpty()) {
            shortUrlId = "1L9zO9O" // TODO call the bse62 convertor here
            val urlModel = UrlModel(shortUrlId,longUrl)
            urlRepository.save(urlModel)
        } else {
            shortUrlId = url[0]!!.shortUrl
        }
        return shortUrlId
    }

    /**
     * Fetches the [longUrl] from db and returns it
     * @param shortUrl
     * @return long url corresponding to the [shortUrl] passed
     */
    override fun getLongUrlFromShortUrl(shortUrl: String): String? {
        logger.info("Short :" + shortUrl)
        val longUrl: String?
        val a = urlRepository.findAll();
        val urlModelList = urlRepository.findByShortUrl(shortUrl)
        logger.info("List :" + urlModelList);
        longUrl = if (urlModelList!!.isEmpty()) {
            null
        } else {
            urlModelList[0]!!.longUrl
        }
        return longUrl
    } /*public Map<String,String> changeId(String id,String url) {
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

    companion object {
        private const val counter = 100000000000L
    }
}
