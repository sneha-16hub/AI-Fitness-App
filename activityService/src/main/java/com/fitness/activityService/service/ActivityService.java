package com.fitness.activityService.service;

import com.fitness.activityService.model.Activity;
import com.fitness.activityService.model.ActivityType;
import com.fitness.activityService.repository.ActivityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.fitness.activityService.dto.ActivityRequest;
import com.fitness.activityService.dto.ActivityResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ActivityService {
    private ActivityRepository repository;
    private UserValidationService userValidationService;
    public ActivityResponse trackActivity(ActivityRequest request){
        boolean isValidUser=userValidationService.validateUser(request.getUserId());
        if(!isValidUser){
            throw new RuntimeException("Invalid user"+request.getUserId());
        }
        Activity activity=Activity.builder()
                .userId(request.getUserId())
                .activityType(ActivityType.valueOf(request.getActivityType()))
                .caloriesBurned(request.getCaloriesBurned())
                .duration(request.getDuration())
                .startTime(LocalDateTime.parse(request.getStartTime()))
                .additionalMetrics(request.getAdditionalMetrics())
                .build();


        Activity savedActivity=repository.save(activity);
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
