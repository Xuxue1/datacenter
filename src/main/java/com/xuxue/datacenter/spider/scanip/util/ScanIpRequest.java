package com.xuxue.datacenter.spider.scanip.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by HanHan on 2016/7/23.
 */
public class ScanIpRequest implements Serializable{

    public final int requestSize;

    public final List<byte[]> scantedIP;

    public ScanIpRequest(int requestSize,List<byte[]> scantedIP){
        this.requestSize=requestSize;
        this.scantedIP=scantedIP;
    }


    public byte[] getByte()throws IOException{
        ObjectOutputStream o=null;
        try{
            ByteArrayOutputStream out=new ByteArrayOutputStream();
            o=new ObjectOutputStream(out);
            o.writeObject(this);
            byte[] data=out.toByteArray();
            return data;
        }finally{
            o.close();
        }
    }

    public void writeSizedStream(OutputStream out)throws IOException{
        byte[] data=getByte();
        ByteBuffer buffer=ByteBuffer.allocate(4);
        buffer.putInt(data.length);
        buffer.flip();
        byte[] size=buffer.array();
        out.write(size,0,size.length);
        out.write(data,0,data.length);
    }

    public static ScanIpRequest readSizeStream(InputStream in)throws IOException,ClassNotFoundException{
        byte[] size=new byte[4];
        in.read(size,0,size.length);
        byte[] data=new byte[byteToInt(size)];
        in.read(data,0,data.length);
        ByteArrayInputStream bin=new ByteArrayInputStream(data);
        ObjectInputStream oin=new ObjectInputStream(bin);
        return (ScanIpRequest) oin.readObject();
    }


    public static int byteToInt(byte[] data){
        ByteBuffer buffer=ByteBuffer.wrap(data);
        return buffer.getInt();
    }

    public static void main(String[] args){
        byte[] data=new byte[]{1,2,3,4};
        System.out.println(byteToInt(data));
    }
}
