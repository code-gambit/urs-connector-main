package com.urlshortner.discovery;

import org.apache.zookeeper.ZooKeeper;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryProperties;
import org.springframework.cloud.zookeeper.discovery.ZookeeperInstance;

import java.util.Collection;
import java.util.Optional;

/*Implementation of Serviceregistry interface. This class provides methods to connect with zooKeeper server.
After connection is established, a zookeeper instance is returned and is mapped by using ServiceInstanceMapper class.
 */
public class ServiceRegistryImpl implements Serviceregistry {

    CuratorFramework curatorFramework;
    ServiceDiscovery<ZookeeperInstance> serviceDiscovery;
    ZookeeperDiscoveryProperties zookeeperDiscoveryProperties;

    public ServiceRegistryImpl(CuratorFramework curatorFramework,
                               ZookeeperDiscoveryProperties zookeeperDiscoveryProperties) {
        this.curatorFramework = curatorFramework;
        this.zookeeperDiscoveryProperties = zookeeperDiscoveryProperties;
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ZookeeperInstance.class).client(curatorFramework)
                .basePath(zookeeperDiscoveryProperties.getRoot()).build();
    }

    @Override
    public ServiceInstanceMapper getInstance(String name){
        try {
            Collection<ServiceInstance<ZookeeperInstance>> serviceInstances = serviceDiscovery.queryForInstances(name);
            Optional<ServiceInstance<ZookeeperInstance>> first= serviceInstances.stream().filter(instance -> StringUtils.equals(instance.getName(),name))
                    .findFirst();

            if(first.isPresent()){
                ServiceInstance<ZookeeperInstance> firstServiceInstance=first.get();
                ServiceInstanceMapper mapper= new ServiceInstanceMapper(firstServiceInstance);
                return mapper;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

    @Override
    public void close() throws Exception{
        if(curatorFramework !=null){
            curatorFramework.close();
            curatorFramework=null;
        }

        if(serviceDiscovery!=null){
            serviceDiscovery.close();
            serviceDiscovery=null;
        }
    }
}
