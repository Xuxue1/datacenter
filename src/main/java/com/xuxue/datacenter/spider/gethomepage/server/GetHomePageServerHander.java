package com.xuxue.datacenter.spider.gethomepage.server;

import com.xuxue.code.io.nio.NIOHandler;
import com.xuxue.code.io.nio.NioEvent;
import com.xuxue.code.util.EndException;
import com.xuxue.datacenter.spider.gethomepage.util.GetHomePageRequest;
import com.xuxue.datacenter.spider.gethomepage.util.GetHomePageResponse;
import com.xuxue.datacenter.spider.gethomepage.util.IPProvider;
import com.xuxue.datacenter.spider.scanip.server.ScanIpConnection;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by HanHan on 2016/7/25.
 */
public class GetHomePageServerHander implements NIOHandler{

    private ConcurrentHashMap<String,GetHomePageConnection> connections;

    private IPProvider provider;

    public GetHomePageServerHander(IPProvider provider){
        this.provider=provider;
        connections=new ConcurrentHashMap<String,GetHomePageConnection>();
    }

    private Logger logger=Logger.getLogger(getClass());

    @Override
    public void processEvent(NioEvent nioEvent) throws IOException, EndException {
        if(nioEvent.key.isValid()&&nioEvent.key.isAcceptable()){
            accept(nioEvent);
        }else if(nioEvent.key.isValid()&&nioEvent.key.isReadable()){
            read(nioEvent);
        }
    }

    public void accept(NioEvent event)throws IOException{
        ServerSocketChannel server=(ServerSocketChannel)event.key.channel();
        SocketChannel socket=server.accept();
        if(socket!=null){
            socket.configureBlocking(false);
            SelectionKey key=socket.register(event.key.selector(), SelectionKey.OP_READ);
            GetHomePageConnection connection=new GetHomePageConnection(socket);
            connections.put(connection.getRemoteIp(),connection);
            key.attach(connection);
            System.out.println("connect a connection");
        }
    }

    public void read(NioEvent event)throws IOException{
        GetHomePageConnection connection=(GetHomePageConnection)event.key.attachment();
        if(connection!=null){
           try{
               GetHomePageRequest req= connection.readRequest();
               int requestIp=req.getRequestIp();
               List<byte[]> ips=provider.getIP(requestIp);
               connection.notifyListener(req.getClientMessage());
               connection.writeResponse(new GetHomePageResponse(null,ips));
               System.out.println("write a massage to client");
           }catch (InterruptedException e){
               throw new IOException();
           }catch (EndException e){
               logger.info("END exception",e);
               throw new IOException();
           }
        }
    }
}
