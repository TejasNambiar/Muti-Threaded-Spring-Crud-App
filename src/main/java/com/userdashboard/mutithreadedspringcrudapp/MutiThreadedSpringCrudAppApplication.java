package com.userdashboard.mutithreadedspringcrudapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
// 1. Tell Spring to only look in the 'postgres' folder for SQL repositories
@EnableJpaRepositories(basePackages = "com.userdashboard.mutithreadedspringcrudapp.repository.postgres")
// 2. Tell Spring to only look in the 'mongo' folder for NoSQL repositories
@EnableMongoRepositories(basePackages = "com.userdashboard.mutithreadedspringcrudapp.repository.mongo")
public class MutiThreadedSpringCrudAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MutiThreadedSpringCrudAppApplication.class, args);
    }

}
