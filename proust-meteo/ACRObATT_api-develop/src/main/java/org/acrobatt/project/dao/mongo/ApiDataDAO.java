package org.acrobatt.project.dao.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.acrobatt.project.model.mongo.ApiData;
import org.acrobatt.project.mongo.MongoClientConfig;
import org.acrobatt.project.mongo.MongoClientFactory;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.mongojack.JacksonDBCollection;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ApiDataDAO {

    private static ApiDataDAO instance;

    //MongoDB client
    private MongoClient mongoClient = MongoClientFactory.getMongoClient();

    public static ApiDataDAO getInstance() throws IOException {
        if(instance == null){
            instance = new ApiDataDAO();
        }
        return instance;
    }
    private ApiDataDAO() throws IOException {}

    public ObjectMapper getJsonMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JsonOrgModule());
        return mapper;
    }


    @SuppressWarnings("unchecked")
    public void insert(ApiData apiData) throws IOException {
        MongoDatabase database = mongoClient.getDatabase(MongoClientConfig.getDatabase());
        MongoCollection collection = database.getCollection(MongoClientConfig.getCollection());

        Document newDocument = Document.parse(getJsonMapper().writeValueAsString(apiData));
        String newDate = ZonedDateTime.now(ZoneId.of( "Europe/Paris" ))
                .truncatedTo(ChronoUnit.MINUTES)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"));

        newDocument.put("_id", new ObjectId());
        newDocument.replace("storedDate", newDate);
        collection.insertOne(newDocument);
    }

    @SuppressWarnings("unchecked")
    public void insertAll(List<ApiData> apiDataList) throws IOException {
        MongoDatabase database = mongoClient.getDatabase(MongoClientConfig.getDatabase());
        MongoCollection collection = database.getCollection(MongoClientConfig.getCollection());

        List<Document> apiDataDocuments = new ArrayList<>();
        for(ApiData a : apiDataList) {
            Document newDocument = Document.parse(getJsonMapper().writeValueAsString(a));
            String newDate = ZonedDateTime.now(ZoneId.of( "Europe/Paris" ))
                    .truncatedTo(ChronoUnit.MINUTES)
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"));

            newDocument.put("_id", new ObjectId());
            newDocument.replace("storedDate", newDate);
            apiDataDocuments.add(newDocument);
        }

        collection.insertMany(apiDataDocuments);
    }

    public List<ApiData> findPage(int page,int size) throws IOException {
        int offset = page * size;
        DB database = mongoClient.getDB(MongoClientConfig.getDatabase());
        DBCollection collection = database.getCollection(MongoClientConfig.getCollection());

        JacksonDBCollection<ApiData, String> coll = JacksonDBCollection.wrap(collection, ApiData.class,
                String.class,getJsonMapper());
        return coll.find().skip(offset).limit(size).toArray();
    }
}
