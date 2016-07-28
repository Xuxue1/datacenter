package com.xuxue.datacenter.spider.scanip.client;

import com.xuxue.datacenter.spider.scanip.util.ScanIpRequest;
import com.xuxue.datacenter.spider.scanip.util.ScanIpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

/**
 * Created by HanHan on 2016/7/24.
 */
public class TestConnect {

    public static void main(String[] args)throws Exception{

        Socket s=new Socket("192.168.24.85",8889);
        InputStream in=s.getInputStream();
        OutputStream out=s.getOutputStream();
        System.out.println("connect");
       for(int i=0;i<1000;i++){
           ScanIpRequest req=new ScanIpRequest(1,null);
           req.writeSizedStream(out);
           System.out.println("write");
           ScanIpResponse res= ScanIpResponse.readSizeStream(in);
           System.out.println(res.getControlMessage());
           List<byte[]> data=res.getIpList();
           System.out.println(data.size());
           Thread.sleep(1000);
       }
    }

}
