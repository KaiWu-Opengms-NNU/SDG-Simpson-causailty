package com.opengms.wukai.tool;


import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;


import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

public class WebService {

    //Get ---无参请求
    public String getMethodNoParameter(String uri){
        String responseString=null;
        CloseableHttpClient httpClient= HttpClientBuilder.create().build();
        // 创建Get请求
        HttpGet httpGet = new HttpGet(uri);
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
//            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
//                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
//                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
                responseString= EntityUtils.toString(responseEntity);
            }
        } catch (ParseException | IOException e) {
            System.out.println("连接失败"+e.getMessage());
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
         }

    }



    //Post ---有参请求
    public String postHaveParameter(String Uri, Object Params,String dtoName){
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(Uri);
        CloseableHttpResponse response = null;
        try {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            ContentType contentType = ContentType.create("text/plain", StandardCharsets.UTF_8);
            if(Params!=null){
                //通过反射获取属性名
                Field[] fields=Class.forName(dtoName).getDeclaredFields();
                for(Field f:fields){
                    f.setAccessible(true);
                    multipartEntityBuilder.addTextBody(f.getName(),f.get(Params).toString(),contentType);
                }
            }
            HttpEntity httpEntity = multipartEntityBuilder.build();
            httpPost.setEntity(httpEntity);
            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
//            System.out.println("HTTPS响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
//                System.out.println("HTTPS响应内容长度为:" + responseEntity.getContentLength());
                // 主动设置编码，来防止响应乱码
                String responseStr = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
                return responseStr;
            }
        } catch (ParseException | IOException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
