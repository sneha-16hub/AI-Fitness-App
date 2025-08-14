package com.fitness.activityService.controller;

import com.fitness.activityService.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fitness.activityService.dto.ActivityRequest;
import com.fitness.activityService.dto.ActivityResponse;
import java.util.List;
@RestController
@RequestMapping("/api/activities")
public class ActivityController {
    @Autowired
    ActivityService service;
    @PostMapping
    private ResponseEntity<ActivityResponse> trackActivity(@RequestBody ActivityRequest request){
        return ResponseEntity.ok(service.trackActivity(request));
    }
    @GetMapping
    private ResponseEntity<List<ActivityResponse>> getActivities(@RequestHeader("X-User-ID") String userId){
        return ResponseEntity.ok(service.getUserActivities(userId));

    }
    @GetMapping("/{activityId}")
    private ResponseEntity<ActivityResponse> getActivity(@PathVariable String activityId){
        return ResponseEntity.ok(service.getActivityById(activityId));

    }

}
