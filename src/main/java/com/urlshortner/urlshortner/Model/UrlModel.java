package com.urlshortner.urlshortner.Model;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

@Table("URL_Table")
@NoArgsConstructor
public class UrlModel implements Serializable {

    @PrimaryKeyColumn(ordinal = 0,type = PrimaryKeyType.PARTITIONED,name = "id")
    @CassandraType(type = CassandraType.Name.BIGINT)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getLongUrl() {
        return longUrl;

    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    private String shortUrl;
    private String longUrl;

    public UrlModel(Long id,String shortUrl, String longUrl) {
        this.id=id;
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
    }
}
