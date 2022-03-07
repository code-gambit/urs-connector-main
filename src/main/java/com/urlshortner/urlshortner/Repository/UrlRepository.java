package com.urlshortner.urlshortner.Repository;
import com.urlshortner.urlshortner.Model.UrlModel;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UrlRepository extends CassandraRepository<UrlModel, Long> {

    @AllowFiltering
    List<UrlModel> findByLongUrl(String longurl);

    @AllowFiltering
    List<UrlModel> findByShortUrl(String shorturl);

    @AllowFiltering
    UrlModel findUrlModelByShortUrl(String shorturl);

    @AllowFiltering
    int countUrlModelByShortUrl(String urlId);

}
