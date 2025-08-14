package com.fitness.activityService.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ActivityRequest {
    private String userId;
    private String activityType;
    private Integer duration; // in minutes
    private Integer caloriesBurned;
    private String startTime;
    private Map<String,Object> additionalMetrics; //
}
