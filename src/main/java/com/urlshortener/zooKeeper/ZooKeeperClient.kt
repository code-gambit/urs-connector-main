package com.urlshortener.zooKeeper

import com.urlshortener.urlshortener.exception.RangeFetchException
import org.apache.zookeeper.CreateMode
import org.apache.zookeeper.KeeperException
import org.apache.zookeeper.Watcher
import org.apache.zookeeper.ZooDefs
import org.apache.zookeeper.ZooKeeper
import org.apache.zookeeper.data.Stat
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.IOException
import java.lang.Exception
import java.lang.InterruptedException
import java.nio.charset.StandardCharsets
import kotlin.Throws

/**
 * Public class for managing zookeeper logic.
 * @param host hostname or IP where zooKeeper is running
 * @param limit the range value or size of single limit
 */
@Service
class ZooKeeperClient(
    @Value("\${zookeeper.host}:\${zookeeper.port}")
    host: String,

    @Value("\${zookeeper.znode.path.counter}")
    private var counterDataPath: String,

    @Value("\${zookeeper.range.limit}")
    private var limit: Int
) {

    private lateinit var conn: ZooKeeperConnection
    private var logger = LoggerFactory.getLogger(javaClass)
    private lateinit var clientId: String
    private var rangeStartVal = -1

    init {
        try {
            conn = ZooKeeperConnection.Builder()
                .host(host).build()
            zookeeperExecutor {
                exists(counterDataPath, false, it) ?: it.create(
                    counterDataPath,
                    "1000000".toByteArray(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * returns the default watcher for the zookeeper
     * @param [zooKeeper] instance of the [ZooKeeper]
     * @return [Watcher]
     */
    private fun getWatcher(zooKeeper: ZooKeeper): Watcher {
        return Watcher { event ->
            if (event.type == Watcher.Event.EventType.NodeChildrenChanged) {
                logger.info("****************** Watcher Start *********************")
                try {
                    logger.info("Watcher applied on client $clientId")
                    testAndSet(zooKeeper)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                logger.info("****************** Watcher End *********************")
            }
        }
    }

    /**
     * Sets the range start value or registers the watch event
     * @throws InterruptedException
     * @throws KeeperException
     */
    @Throws(InterruptedException::class, KeeperException::class)
    private fun testAndSet(zooKeeper: ZooKeeper) {
        val children = getChildren(counterDataPath, false, zooKeeper)
        children.sort()
        if (clientId == "$counterDataPath/" + children[0]) {
            val data = getData(counterDataPath, false, null, zooKeeper)
            val currRange = String(data, StandardCharsets.UTF_8).toInt()
            val updatedRange = (currRange + limit + 1).toString().toByteArray(StandardCharsets.UTF_8)
            setData(counterDataPath, updatedRange, zooKeeper)
            rangeStartVal = currRange
            zooKeeper.close()
        } else {
            zooKeeper.register(getWatcher(zooKeeper))
        }
    }

    /**
     * Fetches the starting value of range for the client
     * @return
     * @throws InterruptedException
     * @throws KeeperException
     */
    @get:Throws(InterruptedException::class, KeeperException::class, IOException::class)
    val rangeForClient: Int
        get() {
            zookeeperExecutor(true) {
                clientId = it.create(
                    "$counterDataPath/client",
                    ByteArray(0),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL
                )
                testAndSet(it)
            }

            zookeeperExecutor {
                while (true) {
                    exists(clientId, false, it) ?: break
                }
            }
            // checks if range was fetched successfully
            if (rangeStartVal == -1) {
                throw RangeFetchException()
            }
            // resets the [rangeStartVal] and returns the current fetch
            val range = rangeStartVal
            rangeStartVal = -1
            return range
        }

    /**
     * Executes the [ZooKeeper] related tasks. It is a wrapper around [ZooKeeper] instance hence it provides a
     * new [ZooKeeper] instance for each call.
     * @param manualConnectionClose if set to false the connection will be automatically closed once task is executed
     * @param func a lambda function which is invoked and [ZooKeeper] instance is passed as an argument
     * @param T the generic return type of the lambda function
     * @return T returns the result of the lambda function [func] execution
     */
    private fun <T> zookeeperExecutor(manualConnectionClose: Boolean = false, func: (zk: ZooKeeper) -> T): T {
        val zookeeperInstance = conn.connect()
        val result = func.invoke(zookeeperInstance)
        if (!manualConnectionClose) {
            zookeeperInstance.close()
        }
        return result
    }

    companion object : ZooKeeperTask {

        override fun getChildren(path: String, watch: Boolean, zookeeperInstance: ZooKeeper): MutableList<String> {
            return zookeeperInstance.getChildren(path, watch)
        }

        override fun getData(path: String, watch: Boolean, stat: Stat?, zookeeperInstance: ZooKeeper): ByteArray {
            return zookeeperInstance.getData(path, watch, stat)
        }

        override fun setData(path: String, data: ByteArray, zookeeperInstance: ZooKeeper): Stat {
            val version: Int? = exists(path, false, zookeeperInstance)?.version
            return zookeeperInstance.setData(path, data, version!!)
        }

        override fun exists(path: String, watch: Boolean, zookeeperInstance: ZooKeeper): Stat? {
            return zookeeperInstance.exists(path, watch)
        }
    }
}
