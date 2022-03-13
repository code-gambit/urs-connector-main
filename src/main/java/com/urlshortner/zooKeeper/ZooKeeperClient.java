package com.urlshortner.zooKeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.watch.IWatchManager;

import java.util.Collections;
import java.util.List;


public class ZooKeeperClient {
    // create static instance for zookeeper class.
    private static ZooKeeper _zk;
    private String _clientId;
    private int numberOfActiveClients;

    // create static instance for ZooKeeperConnection class.
    private static ZooKeeperConnection conn;

    Watcher testWatcher=new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            if(event.getType()== Event.EventType.NodeChildrenChanged){
                System.out.println("****************** Watcher Start *********************");

                try {
                    System.out.println("Watcher applied on client "+_clientId);
                    testAndSet();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("****************** Watcher End *********************");
            }
        }
    };

    public void createZooKeeperClient() {
        try {
            conn = new ZooKeeperConnection();
            _zk = conn.connect("localhost");
            numberOfActiveClients++;

            _clientId = _zk.create("/counter/client", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            List<String> children = _zk.getChildren("/counter", false);
            Collections.sort(children);
            if(_clientId.equals("/counter/"+children.get(0))){
                System.out.println("--------------------------------------");
                System.out.println(children);
                System.out.println(_clientId);
                //counter update read write
                System.out.println("--------------------------------------");
                conn.close();
            }
            else {
                _zk.register(testWatcher);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage()); //Catch error message
        }
    }

    private void testAndSet() throws InterruptedException, KeeperException {
        List<String> children = _zk.getChildren("/counter", false);
        Collections.sort(children);
        if(_clientId.equals("/counter/"+children.get(0))){
            System.out.println("######################################");
            System.out.println(children);
            System.out.println(_clientId);
            //Perform getting the counter and incrementing it

            System.out.println("######################################");
            conn.close();
        }
        else {
            _zk.register(testWatcher);
        }
    }

    public static void main(String[] args) {
        ZooKeeperClient zkObj= new ZooKeeperClient();
        ZooKeeperClient zkObj1= new ZooKeeperClient();
        ZooKeeperClient zkObj2= new ZooKeeperClient();
        for (int i=0;i<6;i++)
            zkObj.createZooKeeperClient();
    }
}
