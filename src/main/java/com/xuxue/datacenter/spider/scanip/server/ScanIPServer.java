package com.xuxue.datacenter.spider.scanip.server;

import com.xuxue.code.io.nio.EventLoop;
import com.xuxue.code.io.nio.NIOServer;
import com.xuxue.code.net.IpList;

import java.io.IOException;
import java.util.List;

/**
 * Created by HanHan on 2016/7/24.
 */
public class ScanIPServer {

    public static void main(String[] args)throws IOException{
        EventLoop loop=new EventLoop(20);
        NIOServer server=new NIOServer(8889,loop);
        loop.addHandler(new ServerHander(new IpList("192.168.0.0","192.169.255.255"),new IPResultBuffer(5000, new IPPipleLine() {
            @Override
            public void save(List<byte[]> ips) {
                System.out.println("save");
            }
        })));

        new Thread(server).start();
    }

}
