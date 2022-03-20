package com.urlshortner.zooKeeper

import org.apache.zookeeper.ZooKeeper
import org.apache.zookeeper.data.Stat

interface ZooKeeperTask {

    fun getChildren(path: String, watch: Boolean = false,zookeeperInstance: ZooKeeper): MutableList<String>

    fun getData(path: String, watch: Boolean = false, stat: Stat? = null,zookeeperInstance: ZooKeeper): ByteArray

    fun setData(path: String, data: ByteArray,zookeeperInstance: ZooKeeper): Stat

    fun exists(path: String, watch: Boolean = false, zookeeperInstance: ZooKeeper ): Stat?

}
