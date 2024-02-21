package com.opengms.wukai.tool;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;



import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MongoUtils {

    private static Properties properties;
    private static MongoDatabase mongoDatabase;
    private static InputStream stream = null;
    private static String host;
    private static int port;
    private static String dbname;
    private static String username;
    private static String password;


    /*加载静态变量*/
    static {
        if (properties == null){
            properties = new Properties();
        }

        try {
            stream = MongoUtils.class.getClassLoader().getResourceAsStream("mongodb.properties");
            properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        host = properties.getProperty("host");
        port = Integer.parseInt(properties.getProperty("port"));
        dbname = properties.getProperty("dbname");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
    }

    /*定义方法，获取MongoDB连接对象*/
    /*定义方法，获取MongoDB连接对象*/
    public static MongoClient getMongoClient(){
        String addr = "mongodb://"  + host + ":" + port;
//        String addr = "mongodb://" + username + ":" + password + "@" + host + ":" + port + "/" + dbname+"?authSource=admin";
        MongoClient mongoClient = MongoClients.create(addr);
        return mongoClient;
    }


}
