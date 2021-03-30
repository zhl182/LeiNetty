package com.zhl.netty.server.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "lei.netty.udp")
public class UDPServerProperties {
    private int port=9999;
    private boolean useBroadcast=false;
    private boolean enabled = true;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isUseBroadcast() {
        return useBroadcast;
    }

    public void setUseBroadcast(boolean useBroadcast) {
        this.useBroadcast = useBroadcast;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
