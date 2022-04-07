package com.zhl.netty.server.thread.factory;

/**
 * @author zhaohl
 * 生成守护线程的线程工厂
 */
public class DaemonThreadFactory {
    public static Thread newThread(Runnable runnable){
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        return thread;
    }
}
