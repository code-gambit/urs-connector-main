package com.urlshortner.zooKeeper

import kotlin.Throws
import java.lang.InterruptedException
import org.apache.zookeeper.KeeperException
import java.io.IOException
import org.apache.zookeeper.ZooDefs
import org.apache.zookeeper.CreateMode
import org.apache.zookeeper.data.Stat
import org.apache.zookeeper.ZooKeeper
import org.apache.zookeeper.Watcher
import org.slf4j.LoggerFactory
import java.lang.Exception
import java.nio.charset.StandardCharsets

/**
 * Public class for managing zookeeper logic.
 * @param host hostname or IP where zooKeeper is running
 * @param limit the range value or size of single limit
 */
class ZooKeeperClient(
        host: String,
        private var limit: Int) {

    private var logger = LoggerFactory.getLogger(javaClass)
    private var clientId: String? = null
    private var rangeStartVal = 0

    private fun setRangeStartVal(rangeStartVal: Int) {
        this.rangeStartVal = rangeStartVal
    }

    fun setLimit(limit: Int) {
        this.limit = limit
    }

    private var testWatcher = Watcher { event ->
        if (event.type == Watcher.Event.EventType.NodeChildrenChanged) {
            logger.info("****************** Watcher Start *********************")
            try {
                logger.info("Watcher applied on client $clientId")
                testAndSet()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            logger.info("****************** Watcher End *********************")
        }
    }

    init {
        try {
            conn = ZooKeeperConnection.Builder()
                    .host(host).build()
            zk = conn!!.connect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Sets the range start value or registers the watch event
     * @throws InterruptedException
     * @throws KeeperException
     */
    @Throws(InterruptedException::class, KeeperException::class)
    private fun testAndSet() {
        val children = getChildren("/counter")
        children.sort()
        if (clientId == "/counter/" + children[0]) {
            logger.info("######################################")
            logger.info(clientId)
            val data = getData("/counter")
            val currRange = String(data).toInt()
            val updatedRange = (currRange + limit).toString().toByteArray(StandardCharsets.UTF_8)
            setData("/counter", updatedRange)
            setRangeStartVal(currRange)
            logger.info("######################################")
            conn!!.close()
        } else {
            zk!!.register(testWatcher)
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
            clientId = zk!!.create("/counter/client",
                    ByteArray(0),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL)
            testAndSet()
            while (true) {
                znodeExists(clientId!!) ?: break
            }
            return rangeStartVal
        }

    companion object: ZooKeeperTask {
        var zk: ZooKeeper? = null
        private var conn: ZooKeeperConnection? = null
        private var logger = LoggerFactory.getLogger(javaClass)

        /**
         * checks if znode exists or not
         * @param path the znode path
         * @return Stat
         * @throws KeeperException
         * @throws InterruptedException
         */
        @Throws(KeeperException::class, InterruptedException::class, IOException::class)
        fun znodeExists(path: String): Stat? {
            zk = conn!!.connect()
            val result = exists(path, true)
            conn!!.close()
            return result
        }

        override fun getChildren(path: String, watch: Boolean): MutableList<String> {
            return zk!!.getChildren(path, watch)
        }

        override fun getData(path: String, watch: Boolean, stat: Stat?): ByteArray {
            return zk!!.getData(path, watch, stat)
        }

        override fun setData(path: String, data: ByteArray): Stat {
            val version: Int = exists(path).version
            return zk!!.setData(path, data, version)
        }

        override fun exists(path: String, watch: Boolean): Stat {
            return zk!!.exists(path, watch)
        }
    }
}
