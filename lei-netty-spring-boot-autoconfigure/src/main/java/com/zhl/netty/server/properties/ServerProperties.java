package com.zhl.netty.server.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@ConfigurationProperties(prefix = "lei.netty")
public class ServerProperties {
    private int port = 9999;
    private String host = "127.0.0.1";
    private int bossCount = 1;
    private int workCount = 8;
    private int aggregatorMaxContentLength=8192;
    private String wsContextPath = "/ws";
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getBossCount() {
        return bossCount;
    }

    public void setBossCount(int bossCount) {
        this.bossCount = bossCount;
    }

    public int getWorkCount() {
        return workCount;
    }

    public void setWorkCount(int workCount) {
        this.workCount = workCount;
    }

    public int getAggregatorMaxContentLength() {
        return aggregatorMaxContentLength;
    }

    public void setAggregatorMaxContentLength(int aggregatorMaxContentLength) {
        this.aggregatorMaxContentLength = aggregatorMaxContentLength;
    }

    public String getWsContextPath() {
        return wsContextPath;
    }

    public void setWsContextPath(String wsContextPath) {
        this.wsContextPath = wsContextPath;
    }
}
