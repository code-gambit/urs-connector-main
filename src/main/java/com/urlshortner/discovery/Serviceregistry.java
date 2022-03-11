package com.urlshortner.discovery;

//An interface to provide functions for connecting with zooKeeper server
public interface Serviceregistry extends AutoCloseable{
    ServiceInstanceMapper getInstance(String name);
}
