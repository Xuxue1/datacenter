package com.xuxue.datacenter.spider.scanip.client;

import com.xuxue.datacenter.spider.scanip.util.ScanIpRequest;
import com.xuxue.datacenter.spider.scanip.util.ScanIpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by HanHan on 2016/7/24.
 */
public class ClientConnect implements Runnable{

    public static final int NULL=0;

    public static final int STAR=2;

    public static final int WAIT=3;

    public static final int EXIT=4;

    private Socket socket;

    private InputStream in;

    private OutputStream out;

    private volatile boolean isExit;

    private BlockingQueue<byte[]> queue;
    /**
     * 扫描结果的缓存
     */
    private LinkedList<byte[]> resultBuffer;

    private boolean isWait;

    private int controlMessage;

    private Object writeSocketLock=new Object();

    public ClientConnect(String serverIP,int port)throws IOException {
        Socket s=new Socket(serverIP,port);
        in=s.getInputStream();
        out=s.getOutputStream();
        isExit=false;
        queue=new ArrayBlockingQueue<byte[]>(5000);
        resultBuffer=new LinkedList<byte[]>();
        this.controlMessage=0;
    }


    public void writeRequest(ScanIpRequest req)throws IOException{
        req.writeSizedStream(out);
    }

    public ScanIpResponse readResponse()throws IOException,ClassNotFoundException{
        return ScanIpResponse.readSizeStream(in);
    }

    //TODO
    private void setControlMessage(int controlMessage){

    }



    @Override
    public void run() {
        if(!isExit){
            try{
                ScanIpResponse res=readResponse();

            }catch (IOException e){

            }catch (ClassNotFoundException e){

            }
        }
    }
}
