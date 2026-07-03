package com.userdashboard.mutithreadedspringcrudapp.dto;

import com.userdashboard.mutithreadedspringcrudapp.model.UserActivity;
import com.userdashboard.mutithreadedspringcrudapp.model.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardView {
    private UserProfile profile;
    private List<UserActivity> activities;
}