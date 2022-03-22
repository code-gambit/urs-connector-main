package com.urlshortner.zooKeeper

import org.apache.zookeeper.CreateMode
import org.apache.zookeeper.KeeperException
import org.apache.zookeeper.Watcher
import org.apache.zookeeper.ZooDefs
import org.apache.zookeeper.ZooKeeper
import org.apache.zookeeper.data.Stat
import org.slf4j.LoggerFactory
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
class ZooKeeperClient(
    host: String,
    private var counterDataPath: String,
    private var limit: Int
) {

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
            exists(counterDataPath, false, zk!!) ?: zk!!.create(
                counterDataPath,
                "0".toByteArray(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT
            )
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
        val children = getChildren(counterDataPath, false, zk!!)
        children.sort()
        if (clientId == "$counterDataPath/" + children[0]) {
            val data = getData(counterDataPath, false, null, zk!!)
            val currRange = String(data, StandardCharsets.UTF_8).toInt()
            val updatedRange = (currRange + limit).toString().toByteArray(StandardCharsets.UTF_8)
            setData(counterDataPath, updatedRange, zk!!)
            setRangeStartVal(currRange)
            zk!!.close()
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
            clientId = zk!!.create(
                "$counterDataPath/client",
                ByteArray(0),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL
            )
            testAndSet()
            val zk_temp = conn!!.connect() // temporary connection to check existence
            while (true) {
                exists(clientId!!, false, zk_temp) ?: break
            }
            zk_temp.close()
            return rangeStartVal
        }

    companion object : ZooKeeperTask {
        var zk: ZooKeeper? = null
        private var conn: ZooKeeperConnection? = null
        private var logger = LoggerFactory.getLogger(javaClass)

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
