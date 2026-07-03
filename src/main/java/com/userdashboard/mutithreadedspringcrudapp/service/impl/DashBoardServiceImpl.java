package com.userdashboard.mutithreadedspringcrudapp.service.impl;

import com.userdashboard.mutithreadedspringcrudapp.model.UserActivity;
import com.userdashboard.mutithreadedspringcrudapp.model.UserProfile;
import com.userdashboard.mutithreadedspringcrudapp.repository.mongo.UserActivityRepository;
import com.userdashboard.mutithreadedspringcrudapp.repository.postgres.UserProfileRepository;
import com.userdashboard.mutithreadedspringcrudapp.service.DashBoardService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class DashBoardServiceImpl implements DashBoardService {

    private final UserProfileRepository profileRepo;
    private final UserActivityRepository activityRepo;

    public DashBoardServiceImpl(UserProfileRepository profileRepo, UserActivityRepository activityRepo){
        this.profileRepo = profileRepo;
        this.activityRepo = activityRepo;
    }

    /**
     * ASYNC TASK 1: Fetch Profile from Postgres
     * This will run entirely on a separate background thread.
     */
    @Override
    @Async("dashboardExecutor")
    public CompletableFuture<UserProfile> fetchProfileAsync(Long userId) {
        // Print statement so you can literally see the thread names switching in your console
        System.out.println("--> FETCHING PROFILE on thread: " + Thread.currentThread().getName());

        UserProfile profile = profileRepo.findById(userId).orElse(null);

        return CompletableFuture.completedFuture(profile);
    }

    /**
     * ASYNC TASK 2: Fetch Activities from MongoDB
     * This runs concurrently alongside Task 1.
     */
    @Override
    @Async("dashboardExecutor")
    public CompletableFuture<List<UserActivity>> fetchActivitiesAsync(Long userId) {
        System.out.println("--> FETCHING ACTIVITIES on thread: " + Thread.currentThread().getName());
        List<UserActivity> activities = activityRepo.findByUserId(userId);
        return CompletableFuture.completedFuture(activities);
    }

    /**
     * BACKGROUND FIRE-AND-FORGET TASK: Log a user activity
     * Since this is a void method, the main controller thread doesn't have to wait
     * for MongoDB to finish saving. It returns an instant response to the user.
     */
    @Override
    @Async("dashboardExecutor")
    public void logActivityAsync(Long userId, String action) {
        System.out.println("--> LOGGING BACKGROUND ACTIVITY [" + action + "] on thread: " + Thread.currentThread().getName());

        UserActivity activity = new UserActivity(userId, action);
        activityRepo.save(activity); // This will finally force Mongo to create your collection!
    }

    // Synchronous Save helper for creating new users in Postgres
    @Override
    public UserProfile saveProfile(UserProfile profile) {
        return profileRepo.save(profile);
    }
}
