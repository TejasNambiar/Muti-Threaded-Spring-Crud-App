package com.userdashboard.mutithreadedspringcrudapp.service;

import com.userdashboard.mutithreadedspringcrudapp.model.UserActivity;
import com.userdashboard.mutithreadedspringcrudapp.model.UserProfile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DashBoardService {
    CompletableFuture<UserProfile> fetchProfileAsync(Long userId);
    CompletableFuture<List<UserActivity>> fetchActivitiesAsync(Long userId);
    void logActivityAsync(Long userId, String action);
    UserProfile saveProfile(UserProfile profile);
}
