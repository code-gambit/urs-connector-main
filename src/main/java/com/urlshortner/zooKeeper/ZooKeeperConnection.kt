package com.urlshortner.zooKeeper

import org.apache.zookeeper.ZooKeeper
import java.util.concurrent.CountDownLatch
import kotlin.Throws
import java.io.IOException
import java.lang.InterruptedException
import org.apache.zookeeper.WatchedEvent
import org.apache.zookeeper.Watcher.Event.KeeperState
import java.lang.NullPointerException

/**
 * Public class for managing ZooKeeper connection
 */
class ZooKeeperConnection
/**
 * Constructor
 * @param host hostname or IP where zookeeper ensemble is running
 */(private val host: String?) {
    private var zoo: ZooKeeper? = null
    private val connectedSignal = CountDownLatch(1)

    /**
     * Method to connect zookeeper ensemble.
     * @return ZooKeeper the instance of [ZooKeeper]
     * @throws IOException
     * @throws InterruptedException
     */
    @Throws(IOException::class, InterruptedException::class)
    fun connect(): ZooKeeper {
        zoo = ZooKeeper(host, 5000) { we: WatchedEvent ->
            if (we.state == KeeperState.SyncConnected) {
                connectedSignal.countDown()
            }
        }
        connectedSignal.await()
        return zoo as ZooKeeper
    }

    /**
     * Disconnects the current instance on this class from zookeeper server
     * @throws InterruptedException
     */
    @Throws(InterruptedException::class)
    fun close() {
        zoo!!.close()
    }

    class Builder {
        private var host: String? = null

        fun host(host: String?): Builder {
            this.host = host
            return this
        }

        fun build(): ZooKeeperConnection {
            if (host == null || host!!.isEmpty()) {
                throw NullPointerException("Host can't be null")
            }
            return ZooKeeperConnection(host)
        }
    }
}
