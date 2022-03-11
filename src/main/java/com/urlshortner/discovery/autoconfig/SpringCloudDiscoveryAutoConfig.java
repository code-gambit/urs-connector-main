package com.urlshortner.discovery.autoconfig;

import com.urlshortner.discovery.Serviceregistry;
import com.urlshortner.discovery.ServiceRegistryImpl;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCloudDiscoveryAutoConfig {

    @Bean
    public Serviceregistry ServiceRegistryImpl(CuratorFramework curatorFramework,
                                               ZookeeperDiscoveryProperties zookeeperDiscoveryProperties){
        return new ServiceRegistryImpl(curatorFramework,zookeeperDiscoveryProperties);
    }
}