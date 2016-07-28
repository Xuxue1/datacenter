package com.xuxue.datacenter.spider.gethomepage.client;

import com.xuxue.code.http.ConfiguredHttpClient;
import com.xuxue.code.http.DocumentTools;
import com.xuxue.code.scala.spider.IPList;
import com.xuxue.code.util.EndException;
import com.xuxue.datacenter.spider.gethomepage.bean.HomePageBean;
import com.xuxue.datacenter.spider.gethomepage.util.HomePageBeanBuffer;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by HanHan on 2016/7/25.
 */
public class GetHomePageEventLoop {

    private ClientConnection conn;

    private ExecutorService pool;

    private List<Thread> threads;

    private List<LoopThread> loops;

    private int threadNumber;

    private HomePageBeanBuffer buffer;

    private ConfiguredHttpClient client;

    private Logger logger=Logger.getLogger(getClass());

    public GetHomePageEventLoop(int threadNumber, ClientConnection connection){
        this.threadNumber=threadNumber;
        this.conn=connection;
        pool= Executors.newCachedThreadPool();
        client=new ConfiguredHttpClient();
        threads=new ArrayList<Thread>();
        loops=new ArrayList<LoopThread>();
        buffer=new HomePageBeanBuffer();
        for(int i=0;i<threadNumber;i++){
            LoopThread t=new LoopThread(client);
            loops.add(t);
            Thread thread=new Thread(t);
            threads.add(thread);
            pool.execute(thread);
        }
    }

    public synchronized void addThread(){
        LoopThread t=new LoopThread(client);
        loops.add(t);
        Thread thread=new Thread(t);
        threads.add(thread);
        pool.execute(thread);
        threadNumber+=1;
    }

    public synchronized void subThread(){
        LoopThread t=loops.remove(loops.size()-1);
        threads.remove(loops.size()-1);
        t.stop();
        threadNumber-=1;
    }

    class LoopThread implements Runnable{

        private Logger logger=Logger.getLogger(getClass());


        ConfiguredHttpClient client;

        private boolean run=true;

        public LoopThread(ConfiguredHttpClient client){
            this.client=client;
        }

        public synchronized void stop(){
            run=false;
        }

        @Override
        public void run() {

            while(true){
               try{
                   logger.info("Event loop Thread start");
                   byte[] ip=conn.getIP();
                   logger.info("get a ip and will getHome page");
                   String ipString= IPList.bytesToipString(ip);
                   HttpGet get=new HttpGet("http://"+ipString);
                   RequestConfig config=RequestConfig.custom().setConnectTimeout(5000)
                           .setSocketTimeout(5000)
                           .setConnectionRequestTimeout(5000)
                           .setMaxRedirects(5).build();
                   get.setConfig(config);
                   Document doc= DocumentTools.toHtml(get,client);
                   HomePageBean bean=new HomePageBean(doc,get.getURI(),ip);

                   buffer.buffer(bean);
               }catch (InterruptedException e){
                    logger.info("break");
                    break;
               }catch (IOException e){
                    logger.info("IOException",e.getCause());
                    continue;
               }catch (EndException e){
                   logger.info("end");
                   break;
               }
            }
        }
    }

}
