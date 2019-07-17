package org.acrobatt.project.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import java.io.IOException;

/**
 * A factory used to get new MongoDB connections.
 */
public class MongoClientFactory {

    private static MongoClient mongoClient;

    private MongoClientFactory() {}

    /**
     * Creates a new MongoDB connection and disposes of the previous one
     * @return a new connection
     * @throws IOException if the connection fails
     */
    public static MongoClient refreshConnection() throws IOException {
        if(mongoClient != null) {
            mongoClient.close();
        }
        mongoClient = configureMongoClient();
        return mongoClient;
    }

    /**
     * Creates or retrieves a MongoDB connection instance
     * @return a new connection
     * @throws IOException if the connection fails
     */
    public static MongoClient getMongoClient() throws IOException {
        if(mongoClient == null){
            mongoClient = configureMongoClient();
        }
        return mongoClient;
    }

    /**
     * Configures a new MongoDB connection based on its configuration
     * @see MongoClientConfig
     * @return a configured connection
     * @throws IOException if the connection fails
     */
    private static MongoClient configureMongoClient() throws IOException {
        if(!MongoClientConfig.isNeedAuth()){
            return new MongoClient(new ServerAddress(MongoClientConfig.getHost(),MongoClientConfig.getPort()));
        }
        MongoCredential credential = MongoCredential.createScramSha1Credential(
                MongoClientConfig.getLogin(),
                MongoClientConfig.getDatabase(),
                MongoClientConfig.getPassword().toCharArray());
        return new MongoClient(
                new ServerAddress(MongoClientConfig.getHost(), MongoClientConfig.getPort()),
                credential,
                new MongoClientOptions.Builder().build());
    }

}
