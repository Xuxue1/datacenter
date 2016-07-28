package com.xuxue.datacenter.spider.gethomepage.server;

import com.xuxue.code.io.nio.EventLoop;
import com.xuxue.code.io.nio.NIOServer;
import com.xuxue.code.sql.ConnectionPool;
import com.xuxue.datacenter.spider.gethomepage.util.IPProvider;

import java.io.IOException;

/**
 * Created by HanHan on 2016/7/26.
 */
public class GetHomePageServer {

    public static void main(String[] args)throws IOException {
        EventLoop loop=new EventLoop(1);
        NIOServer server=new NIOServer(8888,loop);
        ConnectionPool pool=new ConnectionPool(5,"192.168.27.34","xuxue","qwer","search");
        IPProvider provider=new IPProvider(pool,3);
        loop.addHandler(new GetHomePageServerHander(provider));
        new Thread(server).start();
    }
}
