package com.userdashboard.mutithreadedspringcrudapp.config;


import com.userdashboard.mutithreadedspringcrudapp.model.UserActivity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

@Configuration
public class MongoInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final MongoTemplate mongoTemplate;
    @Value("${spring.app.mongodb.activity-collection}")
    String collectionName;

    public MongoInitializer(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        // 1. Explicitly check and pre-create the collection on startup
        if(!mongoTemplate.collectionExists(collectionName)){
            System.out.println("--> Production Init: '" + collectionName + "' collection not found. Creating it now...");
            mongoTemplate.createCollection(collectionName);
        }else{
            System.out.println("--> Production Init: '" + collectionName + "' collection verified and ready.");
        }

        // 2. Production Performance: Ensure indexes are built before APIs fire
        // Since we query activities by 'userId', this field MUST be indexed for performance.
        mongoTemplate.indexOps(UserActivity.class)
                .createIndex(new Index().on("userId", Sort.Direction.ASC).named("idx_user_id"));
        System.out.println("--> Production Init: Indexes verified for " + collectionName);
    }
}
