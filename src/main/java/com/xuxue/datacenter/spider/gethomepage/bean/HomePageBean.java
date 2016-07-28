package com.xuxue.datacenter.spider.gethomepage.bean;

import com.xuxue.code.http.ConfiguredHttpClient;
import com.xuxue.code.http.DocumentTools;
import com.xuxue.code.io.StreamRead;
import com.xuxue.code.scala.spider.IPList;
import com.xuxue.code.util.CharUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * Created by HanHan on 2016/7/25.
 */
public class HomePageBean implements Serializable{

    /**
     * 主页的ip地址
     */
    private String ip;

    /**
     * 主页的url
     */
    private String url;

    /**
     * 主页的域名
     */
    private String host;

    /**
     * 主页的关键字
     */
    private String keyWord;

    /**
     * 主页的描述
     */
    private String description;

    /**
     * 主页的字符集
     */
    private String charset;

    /**
     * 获取主页的时间
     */
    private Date getedDate;

    /**
     * 主页能显示的内容
     */
    private String txt;

    /**
     * 主页的标题
     */
    private String title;

    /**
     * url的端口
     */
    private int port;

    /**
     * 网页是否为中文
     */
    private boolean isChinese;

    public HomePageBean(Document doc,URI url,byte[] ip){
        this.title=doc.title();
        this.txt=doc.text();
        this.charset=doc.charset().toString();
        this.description= DocumentTools.getMeta(doc,description);
        this.keyWord=DocumentTools.getMeta(doc,keyWord);
        this.getedDate=new Date();
        this.url=url.toString();
        this.host=url.getHost();
        this.ip= IPList.bytesToipString(ip);
        this.port=url.getPort();
        this.isChinese= CharUtil.isChinese(txt);
    }

    public String toString(){
        StringBuilder b=new StringBuilder();
        b.append("url="+url+"\n");
        b.append("ip="+ip+"\n");
        b.append("isChnese ="+(isChinese)+"\n");
        b.append("title ="+title+"\n"+" description="+description+"\n keyWord="+keyWord+" \nhost="+host);
        b.append("\ntxt="+txt);
        return b.toString();
    }

    public static void main(String[] args)throws IOException{
        ConfiguredHttpClient client=new ConfiguredHttpClient();
        HttpGet get=new HttpGet("http://www.baidu.com");
        //client.execute(get);
        CloseableHttpResponse res=client.execute(get);
        Document doc= StreamRead.toDocument(res.getEntity().getContent(),get.getURI().toString());
        HomePageBean bean=new HomePageBean(doc,get.getURI(),new byte[]{1,2,3,4});
        System.out.println(bean);
    }

    public String getIp() {
        return ip;
    }

    public String getUrl() {
        return url;
    }

    public String getHost() {
        return host;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public String getDescription() {
        return description;
    }

    public String getCharset() {
        return charset;
    }

    public Date getGetedDate() {
        return getedDate;
    }

    public String getTxt() {
        return txt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setGetedDate(Date getedDate) {
        this.getedDate = getedDate;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }
}
