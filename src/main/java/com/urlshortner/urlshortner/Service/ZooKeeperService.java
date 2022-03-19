package com.urlshortner.urlshortner.Service;

import com.urlshortner.zooKeeper.ZooKeeperClient;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class ZooKeeperService {

    Logger logger= LoggerFactory.getLogger(getClass());

    @Value("${zookeeper.host}:${zookeeper.port}")
    private String host;
    private ZooKeeperClient client;

    @PostConstruct
    private void job() throws InterruptedException, KeeperException, IOException {
        logger.info("Host: " + host);
        client = new ZooKeeperClient(host, 1000);
        logger.info("\nZooKeeperClient object created\n");
        /*String id = client._zk.create(
                "/counter",
                "1".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        logger.info("\n\n Client id : \n\n" + id);*/

        int range = client.getRangeForClient();
        logger.info("\n\n Range : \n\n" + range);
    }

}
