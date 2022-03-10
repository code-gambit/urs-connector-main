package com.urlshortner.discovery;

public interface Serviceregistry extends AutoCloseable{
    ServiceInstanceMapper getInstance(String name);
}
