package com.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoDatabase;
import org.hibernate.boot.model.relational.Database;

public class MongoConfig {

    private static MongoClient mongoClient;


    private static MongoClient getMongoClient() {
        if (mongoClient == null) {
            mongoClient = new MongoClient("localhost", 27017);
        }
        return mongoClient;
    }

    public static MongoDatabase connect(String dataBaseName) {
        return getMongoClient().getDatabase(dataBaseName);
    }
}
