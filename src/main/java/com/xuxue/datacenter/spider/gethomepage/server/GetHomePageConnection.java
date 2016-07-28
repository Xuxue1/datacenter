package com.xuxue.datacenter.spider.gethomepage.server;

import com.xuxue.code.io.nio.Connection;
import com.xuxue.code.io.nio.ServerConnection;
import com.xuxue.datacenter.spider.gethomepage.util.ClientMessage;
import com.xuxue.datacenter.spider.gethomepage.util.GetHomePageRequest;
import com.xuxue.datacenter.spider.gethomepage.util.GetHomePageResponse;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HanHan on 2016/7/25.
 */
public class GetHomePageConnection extends ServerConnection<GetHomePageRequest,GetHomePageResponse>{

    private Date createDate;

    private List<ClientMessageListener> listeners;

    public GetHomePageConnection(SocketChannel socketChannel) throws IOException {
        super(socketChannel);
        createDate=new Date();
        listeners=new ArrayList<ClientMessageListener>();
    }

    public void addListener(ClientMessageListener l){
        listeners.add(l);
    }

    public void notifyListener(ClientMessage m){
        for(ClientMessageListener l:listeners){
            l.change(m);
        }
    }

    public void addClientThread(){
        //TODO
    }

    public void subClientThread(){
        //TODO
    }

    public ClientMessage getClientMessage(){
        //TODO
        return null;
    }

    public void startClient(){
        //TODO
    }

    public void exitClient(){
        //TODO
    }

    public void waitClient(){
        //TODO
    }




}
