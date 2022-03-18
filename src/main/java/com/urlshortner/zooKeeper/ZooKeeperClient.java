package com.urlshortner.zooKeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * Public class for managing zookeeper logic.
 */
public class ZooKeeperClient {

    Logger logger= LoggerFactory.getLogger(getClass());
    private static ZooKeeper _zk;
    private String clientId;
    private int limit;
    private int rangeStartVal = 0;
    private static ZooKeeperConnection conn;

    public void setRangeStartVal(int rangeStartVal) {
        this.rangeStartVal = rangeStartVal;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    Watcher testWatcher=new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            if(event.getType()== Event.EventType.NodeChildrenChanged){
                logger.info("****************** Watcher Start *********************");
                try {
                    logger.info("Watcher applied on client "+ clientId);
                    testAndSet();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                logger.info("****************** Watcher End *********************");
            }
        }
    };

    /**
     * Constructor
     * @param host hostname or IP where zooKeeper is running
     * @param limit the range value or size of single limit
     */
    public ZooKeeperClient(String host,int limit) { //limit set the number of unique ids that the client will receive
        this.limit =limit;
        try {
            conn = new ZooKeeperConnection.Builder()
                    .host(host).build();
            _zk = conn.connect();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * checks if znode exists or not
     * @param path the znode path
     * @return Stat
     * @throws KeeperException
     * @throws InterruptedException
     */
    private static Stat znode_exists(String path) throws
            KeeperException,InterruptedException {
        return _zk.exists(path, true);
    }

    /**
     * Sets the range start value or registers the wathc event
     * @throws InterruptedException
     * @throws KeeperException
     */
    private void testAndSet() throws InterruptedException, KeeperException {
        List<String> children = _zk.getChildren("/counter", false);
        Collections.sort(children);
        if(clientId.equals("/counter/"+children.get(0))){
            logger.info("######################################");
            logger.info(clientId);

            byte[] data = _zk.getData("/counter", false, null);
            int currRange = Integer.parseInt(new String(data));

            byte[] updatedRange = String.valueOf(currRange+ limit).getBytes(StandardCharsets.UTF_8);
            _zk.setData("/counter",updatedRange,_zk.exists("/counter",true).getVersion());
            setRangeStartVal(currRange);
            logger.info("######################################");
            conn.close();
        }
        else {
            _zk.register(testWatcher);
        }
    }

    /**
     * Fetches the starting value of range for the client
     * @return
     * @throws InterruptedException
     * @throws KeeperException
     */
    public int getRangeForClient() throws InterruptedException, KeeperException {
        clientId = _zk.create("/counter/client", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        testAndSet();
        while (true){
            Stat stat = znode_exists(clientId);
            if(stat == null){
                break;
            }
        }
        return rangeStartVal;
    }

}


