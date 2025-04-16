package com.mylstech.product.controller;

import com.mylstech.product.dto.request.PlanRequest;
import com.mylstech.product.dto.response.PlanResponse;
import com.mylstech.product.service.PlanService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/plans")
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;
    @PostMapping("/addPlan")
    public ResponseEntity<PlanResponse> addPlan(@RequestBody PlanRequest request) {
       return new ResponseEntity<> (  planService.addPlan(request), HttpStatus.OK);
    }
    @GetMapping("/getPlan")
    public ResponseEntity<List<PlanResponse>> getPlans() {
        return new ResponseEntity<> ( planService.getPlan(), HttpStatus.OK );
    }
    @PutMapping("/updatePlan/{planId}")
    public ResponseEntity<PlanResponse> updatePlan(@PathVariable Long planId, @RequestBody PlanRequest request) {
        return new ResponseEntity<> ( planService.updatePlan(planId,request), HttpStatus.OK );
    }
}
