package com.urlshortner.discovery;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.cloud.zookeeper.discovery.ZookeeperInstance;

// This class Maps the client with related metadata about the client. Here client refers to the zooKeeper client.
public class ServiceInstanceMapper {
    ZookeeperInstance payload;
    private String serviceInstanceName;
    private String serviceInstanceId;

    public ServiceInstanceMapper(ServiceInstance<ZookeeperInstance> zookeeperInstance){
        //System.out.println(zookeeperInstance);

        this.serviceInstanceId=zookeeperInstance.getId();
        System.out.println(this.serviceInstanceId);
        this.serviceInstanceName = zookeeperInstance.getName();
        this.payload=zookeeperInstance.getPayload();
    }

    public ZookeeperInstance getPayload() {
        return payload;
    }

    public String getServiceInstanceName() {
        return serviceInstanceName;
    }

    public String getServiceInstanceId() {
        return serviceInstanceId;
    }

    public void readCounter(){

    }
}
