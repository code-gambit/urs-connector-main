package com.urlshortner.urlshortner.service

import com.urlshortner.urlshortner.model.CounterOperationResult
import com.urlshortner.zooKeeper.ZooKeeperClient
import kotlin.Throws
import java.lang.InterruptedException
import org.apache.zookeeper.KeeperException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.io.IOException
import javax.annotation.PostConstruct

@Service
class ZooKeeperService {

    private var logger = LoggerFactory.getLogger(javaClass)

    @Value("\${zookeeper.host}:\${zookeeper.port}")
    private val host: String? = null

    @Value("\${zookeeper.counterDataPath}")
    private val counterDataPath: String? = null

    @Value("\${zookeeper.range.limit}")
    private val limit: Int? = null

    private var client: ZooKeeperClient? = null

    @Autowired
    var counterService: CounterService? = null

    @Throws(InterruptedException::class, KeeperException::class, IOException::class)
    @PostConstruct
    private fun job() {
        logger.info("Host: $host")
        client = ZooKeeperClient(host!!, counterDataPath!!, limit!!)
        logger.info("\nZooKeeperClient object created\n")

        val range = client!!.rangeForClient
        logger.info("\n\n Range : \n\n$range")
        var result = counterService!!.insertCounterRange(range.toLong(), range.toLong()+limit)
        if (result is CounterOperationResult.Failure) {
            logger.info(result.reason)

        } else logger.info("Success")
    }
}
