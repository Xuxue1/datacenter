package com.xuxue.datacenter.spider.gethomepage.client;

import java.io.IOException;

/**
 * Created by HanHan on 2016/7/26.
 */
public class ClientMain {

    public static void main(String[] args)throws IOException{
        ClientConnection connection=new ClientConnection("192.168.24.85",8888,5000,20);
        Thread t=new Thread(connection);
        t.start();
    }

}
