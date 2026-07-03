package com.userdashboard.mutithreadedspringcrudapp.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user_activities")
public class UserActivity {

    @Id
    private String id; // Mongo IDs are typically Strings (ObjectIds)
    
    private Long userId; // The foreign key linking back to our Postgres User
    private String action;
    private LocalDateTime timestamp;

    public UserActivity(Long userId, String action) {
        this.userId = userId;
        this.action = action;
    }
}