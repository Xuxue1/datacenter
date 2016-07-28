package com.xuxue.datacenter.spider.scanip.util;

import java.io.*;
import java.util.List;

/**
 * Created by HanHan on 2016/7/23.
 */
public class ScanIpResponse implements Serializable{

    private int controlMessage;

    private List<byte[]> ipList;

    public ScanIpResponse(int controlMessage,List<byte[]> ipList){
        this.controlMessage=controlMessage;
        this.ipList=ipList;
    }

    public int getControlMessage() {
        return controlMessage;
    }

    public List<byte[]> getIpList() {
        return ipList;
    }

    public void setControlMessage(int controlMessage) {
        this.controlMessage = controlMessage;
    }

    public void setIpList(List<byte[]> ipList) {
        this.ipList = ipList;
    }


    public static ScanIpResponse readSizeStream(InputStream in)throws IOException,ClassNotFoundException{
        byte[] size=new byte[4];
        in.read(size,0,size.length);
        byte[] data=new byte[ScanIpRequest.byteToInt(size)];
        in.read(data,0,data.length);
        ByteArrayInputStream bin=new ByteArrayInputStream(data);
        ObjectInputStream oin=new ObjectInputStream(bin);
        return (ScanIpResponse) oin.readObject();
    }
}
