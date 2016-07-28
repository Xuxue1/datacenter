package com.xuxue.datacenter.spider.gethomepage.util;

import com.xuxue.code.tools.SerializableTools;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * Created by HanHan on 2016/7/25.
 */
public class ClientMessage implements Serializable{
    //TODO



    public void writeSizedStream(OutputStream out)throws IOException {
        byte[] data= SerializableTools.serializableToBytes(this);
        ByteBuffer buffer=ByteBuffer.allocate(4);
        buffer.putInt(data.length);
        buffer.flip();
        byte[] size=buffer.array();
        out.write(size,0,size.length);
        out.write(data,0,data.length);
    }

    public static ClientMessage readSizeStream(InputStream in)throws IOException,ClassNotFoundException{
        byte[] size=new byte[4];
        in.read(size,0,size.length);
        byte[] data=new byte[byteToInt(size)];
        in.read(data,0,data.length);
        ByteArrayInputStream bin=new ByteArrayInputStream(data);
        ObjectInputStream oin=new ObjectInputStream(bin);
        return (ClientMessage) oin.readObject();
    }

    public static int byteToInt(byte[] data){
        ByteBuffer buffer=ByteBuffer.wrap(data);
        return buffer.getInt();
    }

}
