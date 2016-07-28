package com.xuxue.datacenter.spider.gethomepage.util;


import com.xuxue.code.scala.spider.IPList;
import com.xuxue.code.sql.ConnectionPool;
import com.xuxue.code.sql.GetConnection;
import com.xuxue.code.util.EndException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by HanHan on 2016/7/25.
 */
public class IPProvider {

    private int index;

    private int maxID;

    private ArrayBlockingQueue<byte[]> queue;

    private ConnectionPool connectionPool;

    private boolean exit;


    private Object indexLock=new Object();

    private ExecutorService pool;

    private int readThread;

    private List<Thread> readThreads;

    private List<ReadThread> readers;

    public IPProvider(ConnectionPool pool,int readThread){
        queue=new ArrayBlockingQueue<byte[]>(5000);
        exit=false;
        this.pool= Executors.newCachedThreadPool();
        this.readThread=readThread;
        this.readThreads=new ArrayList<Thread>();
        this.readers=new ArrayList<ReadThread>();
        this.connectionPool=pool;
        for(int i=0;i<readThread;i++){
            ReadThread t=new ReadThread();
            readers.add(t);
            Thread thread=new Thread(t);
            readThreads.add(thread);
            this.pool.execute(thread);
        }
    }

    public List<byte[]> getIP(int ip)throws EndException,InterruptedException{
        if(exit){
            throw new EndException();
        }
        List<byte[]>  list=new ArrayList<byte[]>(ip);
        System.out.println("tack");
        for(int i=0;i<ip;i++){
            list.add(queue.take());
        }
        return list;
    }

    public static void main(String[] args)throws Exception,EndException{

        ConnectionPool pool=new ConnectionPool(10,"192.168.27.34","xuxue","qwer","search");
        System.out.println("init over");
        try{
           IPProvider p=new IPProvider(pool,50);
            System.out.println("create over");
           List<byte[]> data=p.getIP(10);
           for(byte[] d:data){
               System.out.println(IPList.bytesToipString(d));
           }
       }finally {
           // pool.close();
       }
    }


    public List<byte[]> readIP(int size)throws SQLException{
        List<byte[]> data=new ArrayList<byte[]>();
        Connection conn=connectionPool.getConnection();
        PreparedStatement pre=null;
        ResultSet set=null;
        System.out.println("will read ip");
        try{
            System.out.println("readed");
            pre=conn.prepareStatement("select ip from ip where id>? and id<?");
            synchronized (indexLock){
               pre.setInt(1,index);
               pre.setInt(2,index+size);
               index+=size;
            }
            set=pre.executeQuery();
            while(set.next()){
                data.add(set.getBytes(1));
            }
            return data;
        }finally {
            if(set!=null) set.close();
            if(pre!=null) pre.close();
            connectionPool.backConnection(conn);
        }
    }

    class ReadThread implements Runnable{

        @Override
        public void run() {
            while(!exit){
                try{
                    List<byte[]> ips=readIP(1000);
                     for(byte[] b:ips){
                       queue.put(b);
                     }
                }catch (SQLException e){
                    exit=true;
                }catch (InterruptedException e){
                    exit=true;
                }
            }
        }

    }

}
