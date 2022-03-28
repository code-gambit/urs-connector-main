package com.urlshortener.zooKeeper

import org.apache.zookeeper.KeeperException
import org.apache.zookeeper.ZooKeeper
import org.apache.zookeeper.data.Stat

interface ZooKeeperTask {

    /**
     * Return the list of the children of the node of the given path.
     *
     * @param path the node path
     * @param watch whether need to watch this node
     * @param zookeeperInstance the instance of the [ZooKeeper]
     * @return an unordered array of children of the node with the given path
     * @throws IllegalStateException if watch this node with a null default watcher
     * @throws InterruptedException If the server transaction is interrupted.
     * @throws KeeperException If the server signals an error with a non-zero error code.
     */
    fun getChildren(path: String, watch: Boolean = false, zookeeperInstance: ZooKeeper): MutableList<String>

    /**
     * Return the data and the stat of the node of the given path.
     *
     * @param path the given path
     * @param watch whether need to watch this node
     * @param stat the stat of the node
     * @param zookeeperInstance the instance of the [ZooKeeper]
     * @return the data of the node
     * @throws KeeperException If the server signals an error with a non-zero error code
     * @throws IllegalStateException if watch this node with a null default watcher
     * @throws InterruptedException If the server transaction is interrupted.
     */
    fun getData(path: String, watch: Boolean = false, stat: Stat? = null, zookeeperInstance: ZooKeeper): ByteArray

    /**
     * Set the data for the node of the given path if such a node exists
     *
     * @param path the path of the node
     * @param data the data to set
     * @param zookeeperInstance the instance of the [ZooKeeper]
     * @return the state of the node
     * @throws InterruptedException If the server transaction is interrupted.
     * @throws KeeperException If the server signals an error with a non-zero error code.
     * @throws IllegalArgumentException if an invalid path is specified
     */
    fun setData(path: String, data: ByteArray, zookeeperInstance: ZooKeeper): Stat

    /**
     * Return the stat of the node of the given path. Return null if no such a
     * node exists.
     *
     * @param path the node path
     * @param watch whether need to watch this node
     * @param zookeeperInstance the instance of the [ZooKeeper]
     * @return the stat of the node of the given path; return null if no such a
     *         node exists.
     * @throws KeeperException If the server signals an error
     * @throws IllegalStateException if watch this node with a null default watcher
     * @throws InterruptedException If the server transaction is interrupted.
     */
    fun exists(path: String, watch: Boolean = false, zookeeperInstance: ZooKeeper): Stat?
}
