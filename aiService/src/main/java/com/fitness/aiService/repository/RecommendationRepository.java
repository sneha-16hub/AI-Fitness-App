package com.fitness.aiService.repository;

import com.fitness.aiService.model.Recommendation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RecommendationRepository extends MongoRepository<Recommendation,String> {
    List<Recommendation> findByUserId(String userId);
    Optional<Recommendation> findByActivityId(String activityId);
}
