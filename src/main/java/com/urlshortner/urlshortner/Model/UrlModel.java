package com.urlshortner.urlshortner.Model;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;
import java.io.Serializable;

@Table("URL_TABLE")
public class UrlModel implements Serializable {

    @PrimaryKeyColumn(value = "SHORT_URL", type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = CassandraType.Name.VARCHAR)
    private String shortUrl;

    @Column(value = "LONG_URL")
    @CassandraType(type = CassandraType.Name.VARCHAR)
    private String longUrl;

    @Column(value = "TIMESTAMP")
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    private Long timestamp;

    public UrlModel() {}

    public UrlModel(String shortUrl, String longUrl, Long timestamp) {
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
        this.timestamp = timestamp;
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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public static class Builder {

        private String shortUrl;
        private String longUrl;

        public Builder shortUrl(String shortUrl) {
            this.shortUrl = shortUrl;
            return this;
        }

        public Builder longUrl(String longUrl) {
            this.longUrl = longUrl;
            return this;
        }

        public UrlModel build() {
            if(this.shortUrl == null || this.shortUrl.isEmpty()) {
                throw new NullPointerException("Short url can't be null");
            }
            if(this.longUrl == null || this.longUrl.isEmpty()) {
                throw new NullPointerException("Long url can't be null");
            }
            UrlModel object = new UrlModel(shortUrl, longUrl, System.currentTimeMillis());
            return object;
        }

    }

}
