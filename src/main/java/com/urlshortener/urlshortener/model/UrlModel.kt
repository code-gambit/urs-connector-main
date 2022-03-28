package com.urlshortener.urlshortener.model

import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table
import java.io.Serializable

@Table("URL")
class UrlModel(

    @PrimaryKey
    var shortUrl: String,

    var longUrl: String,

    var timestamp: Long

) : Serializable {

    companion object {

        fun getInstance(shortUrl: String, longUrl: String): UrlModel {
            return UrlModel(shortUrl, longUrl, System.currentTimeMillis())
        }
    }
}
