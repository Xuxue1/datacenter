package com.xuxue.datacenter.spider.scanip.server;


import com.xuxue.code.io.nio.NIOHandler;
import com.xuxue.code.io.nio.NioEvent;
import com.xuxue.code.net.IpList;
import com.xuxue.code.util.EndException;
import com.xuxue.datacenter.spider.scanip.util.ScanIpRequest;
import com.xuxue.datacenter.spider.scanip.util.ScanIpResponse;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by HanHan on 2016/7/23.
 */
public class ServerHander implements NIOHandler{

    private Map<String,ScanIpConnection> nioConnections;

    private IpList list;

    private IPResultBuffer buffer;

    private volatile boolean scaned;

    private volatile int connectedThread;

    public ServerHander(IpList list,IPResultBuffer buffer){
        nioConnections=new ConcurrentHashMap<String,ScanIpConnection>();
        this.list=list;
        this.buffer=buffer;
        scaned=false;
        System.out.println("server Hander creat");
    }

    @Override
    public void processEvent(NioEvent nioEvent) throws IOException,EndException {

        System.out.println("process Event Hander");
        if(nioEvent.key.isValid()&&nioEvent.key.isAcceptable()){
            accept(nioEvent);
        }else if(nioEvent.key.isValid()&&nioEvent.key.isReadable()){
            prosessRead(nioEvent);
        }
    }

    public void prosessRead(NioEvent nioEvent)throws IOException,EndException{
       try{
           ScanIpConnection connection=(ScanIpConnection) nioEvent.key.attachment();
           if(connection==null){
               return;
           }
           ScanIpRequest req=connection.readRequest();
           if(req.scantedIP!=null){
               buffer.add(req.scantedIP);
           }
           ScanIpResponse res=new ScanIpResponse(1,list.get(req.requestSize));
           System.out.println("read a request");
           connection.writeResponse(res);
       }catch (ClassNotFoundException e){
           throw new Error();
       }
    }


    public void accept(NioEvent event)throws IOException{
        ServerSocketChannel server=(ServerSocketChannel)event.key.channel();
        SocketChannel socket=server.accept();
        if(socket!=null){
            socket.configureBlocking(false);
            SelectionKey key=socket.register(event.key.selector(), SelectionKey.OP_READ);
            ScanIpConnection connection=new ScanIpConnection(socket);
            nioConnections.put(connection.getRemoteIp(),connection);
            key.attach(connection);
            System.out.println("connect a connection");
        }
    }

    public synchronized void addConnectThread(){
        connectedThread+=1;
    }

    public synchronized void subConnectThread(){
        connectedThread-=1;
    }

    public synchronized int getConnectedThread(){
        return connectedThread;
    }
}
