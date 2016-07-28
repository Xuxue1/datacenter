package com.xuxue.datacenter.spider.scanip.server;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by HanHan on 2016/7/23.
 */
public class IPResultBuffer {

    private LinkedList<byte[]> buffer;


    private final int maxSize;


    private IPPipleLine pipleLine;

    public IPResultBuffer(int maxBufferSize,IPPipleLine pipleLine){
        this.maxSize=maxBufferSize;
        buffer=new LinkedList<byte[]>();
    }

    public synchronized void  add(List<byte[]> ips){
        int capacity=maxSize-buffer.size();
        if(capacity>ips.size()){
            buffer.addAll(ips);
        }else{
            for(int i=0;i<capacity;i++){
                buffer.add(ips.remove(ips.size()-1));
            }
            pipleLine.save(buffer);
            buffer=new LinkedList<byte[]>();
            add(ips);
        }
    }





}
