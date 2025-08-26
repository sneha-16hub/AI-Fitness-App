package com.fitness.activityService.service;

import com.fitness.activityService.model.Activity;
import com.fitness.activityService.model.ActivityType;
import com.fitness.activityService.repository.ActivityRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fitness.activityService.dto.ActivityRequest;
import com.fitness.activityService.dto.ActivityResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {
    private final ActivityRepository repository;
    private final UserValidationService userValidationService;
    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public ActivityResponse trackActivity(ActivityRequest request){
       boolean isValidUser=userValidationService.validateUser(request.getUserId());
       log.info("Validated User:{}",request.getUserId());
//        if(!isValidUser){
//            throw new RuntimeException("Invalid user"+request.getUserId());
//        }
        LocalDateTime startTime;
        if (request.getStartTime() == null) {
            startTime = LocalDateTime.now();
        }else {
            try {
                startTime = LocalDateTime.parse(request.getStartTime());
            } catch (Exception e) {
                throw new RuntimeException("Invalid start time format: " + request.getStartTime());
            }
        }
        Activity activity=Activity.builder()
                .userId(request.getUserId())
                .activityType(ActivityType.valueOf(request.getActivityType()))
                .caloriesBurned(request.getCaloriesBurned())
                .duration(request.getDuration())
                .startTime(startTime)
                .additionalMetrics(request.getAdditionalMetrics())
                .build();


        Activity savedActivity=repository.save(activity);
        //publish to rabbitMQ for Ai processing

        try{
            rabbitTemplate.convertAndSend(exchange,routingKey,savedActivity);

        }catch(Exception e){
            log.error("Failed to publish activity to RabbitMQ", e);
        }
        return mapToResponse(savedActivity);

    }
    private ActivityResponse mapToResponse(Activity activity){
        ActivityResponse response=new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setActivityType(activity.getActivityType());
        response.setDuration(activity.getDuration());
        response.setStartTime(activity.getStartTime());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());
        return response;
    }

    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activities=repository.findByUserId(userId);
        return activities.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public ActivityResponse getActivityById(String activityId) {
        return repository.findById(activityId).map(this::mapToResponse).orElseThrow(()-> new RuntimeException("Activity not found"));
    }
}
