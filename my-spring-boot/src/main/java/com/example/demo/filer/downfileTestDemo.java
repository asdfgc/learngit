package com.example.demo.filer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


/**
  * 下载文件测试类
  * Java原生的API可用于发送HTTP请求，即java.net.URL、java.net.URLConnection，这些API很好用、很常用，
  * 但不够简便；
  * 
  * 1.通过统一资源定位器（java.net.URL）获取连接器（java.net.URLConnection） 
  * 2.设置请求的参数 
  * 3.发送请求
  * 4.以输入流的形式获取返回内容 5.关闭输入流
  * 
  * @author H__D
  *
  */
public class downfileTestDemo {

    /**
      * 
      * @param urlPath  下载路径
      * @param downloadDir  下载存放目录
      * @return 返回下载文件
      * @throws Exception 
      */
         public static void downloadFile(String urlPath, String saveDir) throws Exception {
                 URL url = new URL(urlPath);
                 // 连接类的父类，抽象类
                 URLConnection urlConnection = url.openConnection();
                // http的连接类
                 HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                // 设定请求的方法，默认是GET（对于知识库的附件服务器必须是GET，如果是POST会返回405。流程附件迁移功能里面必须是POST，有所区分。）
                httpURLConnection.setRequestMethod("GET");
                 // 设置字符编码
                 httpURLConnection.setRequestProperty("Charset", "UTF-8");
                // 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
                 int code = httpURLConnection.getResponseCode();


                InputStream inputStream = httpURLConnection.getInputStream();
                //ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //byte[] buffer = new byte[1024];
                //int len = 0;
                //byte[] dataBytes = null;
                //while ((len = inputStream.read(buffer)) != -1 ) {  
                //baos.write(buffer, 0, len);  
                //}
                //baos.flush();
                //dataBytes = baos.toByteArray();
                //InputStream returnInputStream = new ByteArrayInputStream(dataBytes);

                 File file = new File(saveDir);
                 OutputStream out = new FileOutputStream(file);
                 int size = 0;
                 int lent = 0;
                 byte[] buf = new byte[1024];
                 while ((size = inputStream.read(buf)) != -1) {
                    lent += size;
                    out.write(buf, 0, size);
                }
                 inputStream.close();
                 out.close();
 }


    public static void main(String[] args) throws Exception {

        // 下载文件测试
        downloadFile("http://image5.suning.cn/uimg/cms/img/150338612019892140.png", "D:/logo.jpg");

    }
}
