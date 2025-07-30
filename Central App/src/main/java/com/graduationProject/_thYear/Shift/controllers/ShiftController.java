package com.graduationProject._thYear.Shift.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.graduationProject._thYear.Auth.models.User;
import com.graduationProject._thYear.Shift.dtos.requests.CloseShiftRequest;
import com.graduationProject._thYear.Shift.dtos.requests.StartShiftRequest;
import com.graduationProject._thYear.Shift.dtos.responses.ShiftResponse;
import com.graduationProject._thYear.Shift.services.ShiftService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
public class ShiftController {
    private final ShiftService shiftService;

    @PostMapping
    public ResponseEntity<ShiftResponse> startShift(
            @Valid @RequestBody StartShiftRequest request,
            @AuthenticationPrincipal User user
    ) {
        request.setUser(user);
        var response = shiftService.startShift(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShiftResponse> checkShift(@PathVariable Integer id) {
        var response = shiftService.checkShift(id);
        return ResponseEntity.ok(response);
    }

    // @GetMapping
    // public ResponseEntity<List<ShiftResponse>> getAlls() {
    //     List<ShiftResponse> responses = shiftService.getAlls();
    //     return ResponseEntity.ok(responses);
    // }

    @PutMapping("/{id}")
    public ResponseEntity<ShiftResponse> closeShift(
            @PathVariable Integer id,
            @Valid @RequestBody CloseShiftRequest request) {
        var response = shiftService.closeShift(id, request);
        return ResponseEntity.ok(response);
    }



}
