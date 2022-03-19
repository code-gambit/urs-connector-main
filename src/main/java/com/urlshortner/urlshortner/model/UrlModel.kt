package com.urlshortner.urlshortner.model

import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.CassandraType
import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.Table
import java.io.Serializable

@Table("URL")
class UrlModel(

    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = CassandraType.Name.VARCHAR)
    var shortUrl: String,

    @Column
    @CassandraType(type = CassandraType.Name.VARCHAR)
    var longUrl: String,

    @Column
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    var timestamp: Long = System.currentTimeMillis()

) : Serializable
