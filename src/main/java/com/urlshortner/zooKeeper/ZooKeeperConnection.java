package com.urlshortner.zooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * Public class for managing ZooKeeper connection
 */
public class ZooKeeperConnection {

    private String host;
    private ZooKeeper zoo;
    final CountDownLatch connectedSignal = new CountDownLatch(1);

    /**
     * Constructor
     * @param host hostname or IP where zookeeper ensemble is running
     */
    public ZooKeeperConnection(String host) {
        this.host = host;
    }

    /**
     * Method to connect zookeeper ensemble.
     * @return ZooKeeper the instance of [ZooKeeper]
     * @throws IOException
     * @throws InterruptedException
     */
    public ZooKeeper connect() throws IOException,InterruptedException {

        zoo = new ZooKeeper(this.host,5000, we -> {

            if (we.getState() == KeeperState.SyncConnected) {
                connectedSignal.countDown();
            }
        });

        connectedSignal.await();
        return zoo;
    }

    /**
     * Disconnects the current instance on this class from zookeeper server
     * @throws InterruptedException
     */
    public void close() throws InterruptedException {
        zoo.close();
    }

    public static class Builder {
        String host;

        public Builder host(String host) {
            this.host = host;
            return this;
        }

            public ZooKeeperConnection build() {
            if(host == null || host.isEmpty()) {
                throw new NullPointerException("Host can't be null");
            }
            ZooKeeperConnection connection = new ZooKeeperConnection(this.host);
            return connection;
        }

    }
}
