package com.xuxue.datacenter.spider.gethomepage.client;

import com.xuxue.code.util.EndException;
import com.xuxue.datacenter.spider.gethomepage.util.ClientMessage;
import com.xuxue.datacenter.spider.gethomepage.util.ControlMessage;
import com.xuxue.datacenter.spider.gethomepage.util.GetHomePageRequest;
import com.xuxue.datacenter.spider.gethomepage.util.GetHomePageResponse;
import org.apache.log4j.Logger;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * //\\WINDOWS-ILR15LK\file
 * Created by HanHan on 2016/7/25.
 */
public class ClientConnection implements Runnable {

    private Logger logger=Logger.getLogger(getClass());

    private String ip;

    private int port;

    private Socket socket;

    private InputStream inputStream;

    private OutputStream outputStream;

    private volatile LinkedList<byte[]> ips;

    private Object ipLock;

    private volatile boolean isClose;

    private int bufferSize;

    private GetHomePageEventLoop eventloop;

    private volatile boolean isWait;

    private volatile int lockedThread;

    private int totleThread;

    public ClientConnection(String ip,int port,int bufferSize,int loopThread)throws IOException{
        this.bufferSize=bufferSize;
        this.ip=ip;
        this.port=port;
        this.socket=new Socket(ip,port);
        this.inputStream=socket.getInputStream();
        this.outputStream=socket.getOutputStream();
        this.ips=new LinkedList<byte[]>();
        this.ipLock=new Object();
        this.isClose=false;
        isWait=false;
        eventloop=new GetHomePageEventLoop(loopThread,this);
        logger.info("create connection over");
    }

    public void addClientThread(){

    }

    public byte[] getIP()throws InterruptedException,EndException{
       try{
           synchronized (ipLock){
               if(isWait==true||ips.size()==0) {
                   writeRequest(new GetHomePageRequest(5000,getMessage()));
                   ipLock.wait();
               }
               return ips.remove();
           }
       }catch (IOException e){
           throw new EndException();
       }

    }

    public void addIP(List<byte[]> ips){
        synchronized (ipLock){
            if(ips!=null){
                this.ips.addAll(ips);
                notifyLoopThread();
            }
        }
    }

    public void notifyLoopThread(){
        synchronized(ipLock){
            ipLock.notifyAll();
        }
    }

    public void subClientThread(){

    }

    private ClientMessage getMessage(){

        return null;
    }

    private void startClient(){

    }

    private void stopClient(){

    }

    private void exitClient(){

    }

    public void close()throws IOException {

    }

    private void writeRequest(GetHomePageRequest req)throws IOException{
        req.writeSizedStream(outputStream);

    }

    private GetHomePageResponse readResponse()throws IOException,ClassNotFoundException{
        return GetHomePageResponse.readSizeStream(inputStream);
    }

    private void processControlMessage(ControlMessage message)throws IOException{
        switch(message.getMessgae()){
            case ControlMessage.EXIT:{
                close();
                break;
            }
            case ControlMessage.NULL:{
                break;
            }
            case ControlMessage.REPORT:{
                ClientMessage clientMessage=getMessage();
                GetHomePageRequest q=new GetHomePageRequest(0,clientMessage);
                writeRequest(q);
                break;
            }
            case ControlMessage.START:{
                notifyLoopThread();
                break;
            }
            case ControlMessage.WAIT:{
                isWait=true;
                break;
            }
        }
    }



    @Override
    public void run() {

        while(!isClose){
            logger.info("Client connection Thread start");
            try{
                GetHomePageResponse res=readResponse();
                ControlMessage controlMessage=res.getControlMessage();
                System.out.println("read a server message");
                List<byte[]> ips=res.getIps();
                addIP(ips);
                processControlMessage(controlMessage);
            } catch (IOException e){
                isClose=true;
               logger.info("Exception e",e);
                break;

            }catch (ClassNotFoundException e){
                throw new Error();
            }
        }
    }
}
