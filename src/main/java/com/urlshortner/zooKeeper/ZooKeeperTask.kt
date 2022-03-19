package com.urlshortner.zooKeeper

import org.apache.zookeeper.data.Stat

interface ZooKeeperTask {

    fun getChildren(path: String, watch: Boolean = false): MutableList<String>

    fun getData(path: String, watch: Boolean = false, stat: Stat? = null): ByteArray

    fun setData(path: String, data: ByteArray): Stat

    fun exists(path: String, watch: Boolean = true): Stat

}
