package com.fitness.activityService.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.fitness.activityService.model.Activity;
import java.util.List;

public interface ActivityRepository extends MongoRepository<Activity,String> {
    List<Activity> findByUserId(String userId);

}
