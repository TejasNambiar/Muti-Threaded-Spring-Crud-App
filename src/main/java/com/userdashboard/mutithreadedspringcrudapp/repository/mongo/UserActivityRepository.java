package com.userdashboard.mutithreadedspringcrudapp.repository.mongo;

import com.userdashboard.mutithreadedspringcrudapp.model.UserActivity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserActivityRepository extends MongoRepository<UserActivity, String> {
    // Custom query method to find all activities belonging to a specific user
    List<UserActivity> findByUserId(Long userId);
}