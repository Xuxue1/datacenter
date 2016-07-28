package com.xuxue.datacenter.spider.scanip.util;

import org.junit.Before;
import org.junit.Test;

import java.io.*;

/**
 * Created by HanHan on 2016/7/23.
 */
public class ScanIpRequestTest {

    ScanIpRequest request=new ScanIpRequest(10,null);

    ByteArrayInputStream i=null;

    ByteArrayOutputStream o=null;

    ObjectInputStream in=null;

    ObjectOutputStream out=null;
    @Before
    public void befor()throws IOException{
        o=new ByteArrayOutputStream();
        out=new ObjectOutputStream(o);
        out.writeObject(request);
        byte[] data=o.toByteArray();
        out.close();
        System.out.println(data.length);
        i=new ByteArrayInputStream(data);
        in=new ObjectInputStream(i);
    }


    /**
     * 測試 能被序列化的類 如果類裡面有final的變量 能不能正常反序列化
     * @throws IOException
     * @throws ClassNotFoundException
     */

    @Test
    public void test()throws IOException,ClassNotFoundException{

        ScanIpRequest re=(ScanIpRequest) in.readObject();

        System.out.println(re.scantedIP==null);
        System.out.println(re.requestSize);

    }

}
