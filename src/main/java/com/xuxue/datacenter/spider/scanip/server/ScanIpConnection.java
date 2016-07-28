package com.xuxue.datacenter.spider.scanip.server;

import com.xuxue.code.io.nio.Connection;
import com.xuxue.datacenter.spider.scanip.util.ScanIpRequest;
import com.xuxue.datacenter.spider.scanip.util.ScanIpResponse;

import java.io.*;
import java.nio.channels.SocketChannel;
import java.util.Date;

/**
 * Created by HanHan on 2016/7/23.
 */
public class ScanIpConnection extends Connection{

    private Date connectTime;

    private int control;


    public ScanIpConnection(SocketChannel socketChannel) throws IOException{
        super(socketChannel);
        this.connectTime=new Date();
    }


    public ScanIpRequest readRequest()throws IOException,ClassNotFoundException{
        ObjectInputStream oin=null;
        try{
            read();
            byte[] data=getData();
            ByteArrayInputStream in=new ByteArrayInputStream(data);
            oin=new ObjectInputStream(in);
            return (ScanIpRequest) oin.readObject();
        }finally {
            if(oin!=null)
                oin.close();
        }
    }

    public void notifyClientStart(){

    }

    public void notifyClientWait(){

    }

    public void writeResponse(ScanIpResponse res)throws IOException{
        ObjectOutputStream o=null;
        try{
            ByteArrayOutputStream out=new ByteArrayOutputStream();
            o=new ObjectOutputStream(out);
            o.writeObject(res);
            byte[] data=out.toByteArray();
            write(data);
        }finally {
            o.close();
        }

    }

}
