package com.userdashboard.mutithreadedspringcrudapp.controller;

import com.userdashboard.mutithreadedspringcrudapp.dto.DashboardView;
import com.userdashboard.mutithreadedspringcrudapp.model.UserActivity;
import com.userdashboard.mutithreadedspringcrudapp.model.UserProfile;
import com.userdashboard.mutithreadedspringcrudapp.service.DashBoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashBoardService dashboardService;

    public DashboardController(DashBoardService dashboardService){
        this.dashboardService = dashboardService;
    }

    /**
     * 1. CREATE USER & LOG ACTIVITY (Fire-and-Forget Multithreading)
     */
    @PostMapping("/user")
    public ResponseEntity<UserProfile> createUser(@RequestBody UserProfile profile) {
        // Save to Postgres synchronously (we need the generated ID)
        UserProfile savedProfile = dashboardService.saveProfile(profile);

        // TRIGGER THE MONGO THREAD: Asynchronously log this action in Mongo.
        // The controller does NOT wait for Mongo to finish saving. It moves on instantly.
        dashboardService.logActivityAsync(savedProfile.getId(), "USER_CREATED");

        return ResponseEntity.ok(savedProfile);
    }

    /**
     * 2. GET DASHBOARD (Parallel Read Multithreading)
     */
    @GetMapping("/{userId}")
    public CompletableFuture<ResponseEntity<DashboardView>> getUserDashboard(@PathVariable Long userId) {
        System.out.println("Main Request Thread: " + Thread.currentThread().getName());

        // Step A: Kick off both database operations simultaneously
        CompletableFuture<UserProfile> profileFuture = dashboardService.fetchProfileAsync(userId);
        CompletableFuture<List<UserActivity>> activitiesFuture = dashboardService.fetchActivitiesAsync(userId);

        // Step B: Log an audit event in the background that this user looked at their dashboard
        dashboardService.logActivityAsync(userId, "VIEWED_DASHBOARD");

        // Step C: Join the two parallel database threads together when they finish
        return CompletableFuture.allOf(profileFuture, activitiesFuture)
                .thenApply(voidResult -> {
                    // Extract the values from our completed promises
                    UserProfile profile = profileFuture.join();
                    List<UserActivity> activities = activitiesFuture.join();

                    // If user doesn't exist, return 404
                    if (profile == null) {
                        return ResponseEntity.notFound().build();
                    }

                    // Pack them neatly into our view DTO
                    DashboardView dashboard = new DashboardView(profile, activities);
                    return ResponseEntity.ok(dashboard);
                });
    }

}
