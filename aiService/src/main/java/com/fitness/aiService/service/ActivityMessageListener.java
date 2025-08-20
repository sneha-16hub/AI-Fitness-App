package com.fitness.aiService.service;

import com.fitness.aiService.model.Activity;
import com.fitness.aiService.model.Recommendation;
import com.fitness.aiService.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {

    private final ActivityAiService activityAiService;
    private final RecommendationRepository recommendationRepository;

    @RabbitListener(queues="activity.queue")
    public void processActivity(Activity activity){
        log.info("Received activity: {}", activity);
       // log.info("Generated Recommendation:{}", activityAiService.generateRecommendation(activity));
        Recommendation recommendation=activityAiService.generateRecommendation(activity);
        recommendationRepository.save(recommendation);
    }

}
