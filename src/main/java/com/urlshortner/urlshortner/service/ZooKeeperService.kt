package com.urlshortner.urlshortner.service

import com.urlshortner.zooKeeper.ZooKeeperClient
import kotlin.Throws
import java.lang.InterruptedException
import org.apache.zookeeper.KeeperException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.IOException
import javax.annotation.PostConstruct

@Component
class ZooKeeperService {

    private var logger = LoggerFactory.getLogger(javaClass)

    @Value("\${zookeeper.host}:\${zookeeper.port}")
    private val host: String? = null
    private var client: ZooKeeperClient? = null

    @Throws(InterruptedException::class, KeeperException::class, IOException::class)
    private fun job() {
        logger.info("Host: $host")
        client = ZooKeeperClient(host!!, 1000)
        logger.info("\nZooKeeperClient object created\n")
        /*String id = client._zk.create(
                "/counter",
                "1".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        logger.info("\n\n Client id : \n\n" + id);*/
        val range = client!!.rangeForClient
        logger.info("\n\n Range : \n\n$range")
    }
}
