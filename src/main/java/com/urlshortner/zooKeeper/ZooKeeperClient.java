package com.urlshortner.zooKeeper;


import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;


public class ZooKeeperClient {
    Logger logger= LoggerFactory.getLogger(getClass());
    // create static instance for zookeeper class.
    private static ZooKeeper _zk;
    private String _clientId;
    private int LIMIT;
    private int RangeStartVal=0;
    // create static instance for ZooKeeperConnection class.
    private static ZooKeeperConnection conn;

    public void setRangeStartVal(int rangeStartVal) {
        RangeStartVal = rangeStartVal;
    }

    public void setLIMIT(int LIMIT) {
        this.LIMIT = LIMIT;
    }

    Watcher testWatcher=new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            if(event.getType()== Event.EventType.NodeChildrenChanged){
                logger.info("****************** Watcher Start *********************");
                try {
                    logger.info("Watcher applied on client "+_clientId);
                    testAndSet();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                logger.info("****************** Watcher End *********************");
            }
        }
    };

    public ZooKeeperClient(String host,int _limit) { //limit set the number of unique ids that the client will receive
        this.LIMIT=_limit;
        try {
            conn=new ZooKeeperConnection();
            _zk=conn.connect(host);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static Stat znode_exists(String path) throws
            KeeperException,InterruptedException {
        return _zk.exists(path, true);
    }

    private void testAndSet() throws InterruptedException, KeeperException {
        List<String> children = _zk.getChildren("/counter", false);
        Collections.sort(children);
        if(_clientId.equals("/counter/"+children.get(0))){
            logger.info("######################################");
            logger.info(_clientId);

            byte[] data = _zk.getData("/counter", false, null);
            int currRange= Integer.parseInt(new String(data));

            byte[] updatedRange = String.valueOf(currRange+LIMIT).getBytes(StandardCharsets.UTF_8);
            _zk.setData("/counter",updatedRange,_zk.exists("/counter",true).getVersion());
            setRangeStartVal(currRange);
            logger.info("######################################");
            conn.close();
        }
        else {
            _zk.register(testWatcher);
        }
    }


    //Call this method to get the starting value of range for the client
    public int getRangeForClient() throws InterruptedException, KeeperException {
        _clientId = _zk.create("/counter/client", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        testAndSet();
        while (true){
            Stat stat=znode_exists(_clientId);
            if(stat==null){
                break;
            }
        }
        return RangeStartVal;
    }

}


