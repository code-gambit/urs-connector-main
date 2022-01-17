package com.urlshortner.urlshortner.Service;
import com.urlshortner.urlshortner.Model.UrlModel;
import com.urlshortner.urlshortner.Repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UrlService {
    static String base62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final UrlRepository urlRepository;
    @Autowired
    public UrlService(UrlRepository urlRepository){
        this.urlRepository=urlRepository;
    }
    public Map<String,String> createShortUrlFromLongUrl(String longUrl){
        Map<String,String> result=new HashMap<>();
        List<UrlModel> Url=urlRepository.findByLongUrl(longUrl);
        if(Url.size()<1){
            List<Long> hashCodeList=new ArrayList<>();
            List<UrlModel> list=urlRepository.findAll();
            long id;
            if(list.size()!=0) {
                //Find the maximum id already used in the database and increment it by 1 for new entry
                id = urlRepository.getMaxId() + 1;
            }
            else
                id = 1;
            long dividend = id;
            long remainder;//Contains the positions of the letters of hash code.
            while(dividend > 0) {
                remainder = dividend%62;
                dividend = dividend/62;
                hashCodeList.add(0,remainder);
            }
            long hashDigitsCount = hashCodeList.size();
            StringBuilder hashString = new StringBuilder();
            int i = 0;
            while(hashDigitsCount > i) {
                hashString.append(base62.charAt(Math.toIntExact(hashCodeList.get(i))));
                i++;
            }
            UrlModel addNew=new UrlModel(id, hashString.toString(),longUrl);
            hashString.insert(0, "http://localhost:8085/URS/api/test/v1/url/");
            result.put("id", hashString.toString());
            urlRepository.save(addNew);
        }else{
            String out=Url.get(0).getShortUrl();
            result.put("id","http://localhost:8085/URS/api/test/v1/url/"+out);
        }
        return result;
    }

    public Map<String,String> getLongUrlFromShortUrl(String shortUrl){
        Map<String,String> map=new HashMap<>();
        String result;
        List<UrlModel> urlModelList=urlRepository.findByShortUrl(shortUrl);
        if(urlModelList.size()==0)
            result="URL not available";
        else
            result=urlModelList.get(0).getLongUrl();
        map.put("url",result);
        return map;
    }

    public Map<String,String> changeId(String id,String url) {
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
    }
}
