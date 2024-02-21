package com.opengms.wukai.data;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.*;
import com.opengms.wukai.tool.MongoUtils;
import org.bson.Document;
import org.junit.Test;



import java.util.ArrayList;
import java.util.List;

public class DBService {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public DBService(String dbname){
        mongoClient = MongoUtils.getMongoClient();
        mongoDatabase = mongoClient.getDatabase(dbname);
        System.out.println("数据库连接成功！");
    }

    //得到数据库中的所有表名
    @Test
    public void getDBs(){
        MongoIterable<String> databaseNames = mongoClient.listDatabaseNames();
        for (String databaseName : databaseNames) {
            System.out.println(databaseName);
        }
    }

    //向数据库中插入一条数据
    public <T> void insertOneCollection(T data,String collectionName){
        MongoCollection collection=mongoDatabase.getCollection(collectionName);
        Document document=Document.parse(JSONObject.toJSON(data).toString());
        collection.insertOne(document);
    }



    //向数据库插入多条数据
    public void insertCollection(List<?> data, String collectionName){
        MongoCollection collection= mongoDatabase.getCollection(collectionName);
        List<Document> documentList=new ArrayList<Document>();
        //向数据库中插入数据
        for (int i=0;i<data.size();i++){
           Document document=Document.parse(JSONObject.toJSON(data.get(i)).toString());
           documentList.add(document);
        }
        collection.insertMany(documentList);
    }

    //在数据库中查询第一条数据
    public <T> T getFirstOfCollection(Class<T> classType, String collectionName) throws IllegalAccessException, InstantiationException {
        MongoCollection collection= mongoDatabase.getCollection(collectionName);
        Document document=(Document)collection.find().first();
        if(document==null){
            return classType.newInstance();
        }
        T oneData=JSON.toJavaObject(JSONObject.parseObject(document.toJson()),classType);
        return oneData;
    }

    //在数据库中查询所有数据
    public <T> List<T> getCollection(Class<T> classType,String collectionName) {
        MongoCollection collection= mongoDatabase.getCollection(collectionName);
        FindIterable findIterable=collection.find();
        MongoCursor cursor=findIterable.iterator();
        List<T> dataList=new ArrayList<>();
        while (cursor.hasNext()){
            Document document=(Document)cursor.next();
            T oneData=JSON.toJavaObject(JSONObject.parseObject(document.toJson()),classType);
            dataList.add(oneData);
        }
        return dataList;
    }

    @Test
    public void closeClient(){
        mongoClient.close();
    }

}
