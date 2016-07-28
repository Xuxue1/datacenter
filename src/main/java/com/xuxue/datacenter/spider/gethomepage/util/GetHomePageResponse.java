package com.xuxue.datacenter.spider.gethomepage.util;

import com.xuxue.code.tools.SerializableTools;


import java.io.*;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by HanHan on 2016/7/25.
 */
public class GetHomePageResponse implements Serializable{


    private ControlMessage controlMessage;

    private List<byte[]> ips;

    public GetHomePageResponse(ControlMessage message,List<byte[]> ips){
        this.ips=ips;
    }


    public ControlMessage getControlMessage() {
        return controlMessage;
    }

    public void setControlMessage(ControlMessage controlMessage) {
        this.controlMessage = controlMessage;
    }

    public List<byte[]> getIps() {
        return ips;
    }

    public void setIps(List<byte[]> ips) {
        this.ips = ips;
    }

    public void writeSizedStream(OutputStream out)throws IOException {
        byte[] data= SerializableTools.serializableToBytes(this);
        ByteBuffer buffer=ByteBuffer.allocate(4);
        buffer.putInt(data.length);
        buffer.flip();
        byte[] size=buffer.array();
        out.write(size,0,size.length);
        out.write(data,0,data.length);
    }

    public static GetHomePageResponse readSizeStream(InputStream in)throws IOException,ClassNotFoundException{
        byte[] size=new byte[4];
        in.read(size,0,size.length);
        int sizes=byteToInt(size);
        byte[] data=new byte[128];

        ByteArrayOutputStream out=new ByteArrayOutputStream();
        int readed=0;
        while(readed!=sizes){
            int read=in.read(data,0,data.length);
            readed+=read;
            out.write(data,0,read);
        }
        ByteArrayInputStream bin=new ByteArrayInputStream(out.toByteArray());
        ObjectInputStream oin=new ObjectInputStream(bin);
        return (GetHomePageResponse) oin.readObject();
    }

    public static int byteToInt(byte[] data){
        ByteBuffer buffer=ByteBuffer.wrap(data);
        return buffer.getInt();
    }

}
