package com.userdashboard.mutithreadedspringcrudapp.repository;

import com.userdashboard.mutithreadedspringcrudapp.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    // Standard CRUD operations (save, findById, delete) are automatically included!
}