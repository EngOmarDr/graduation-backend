package com.graduationProject._thYear.Advertisements.controllers;

import com.graduationProject._thYear.Advertisements.dtos.requests.CreateAdvertisementRequest;
import com.graduationProject._thYear.Advertisements.dtos.requests.UpdateAdvertisementRequest;
import com.graduationProject._thYear.Advertisements.dtos.responses.AdvertisementResponse;
import com.graduationProject._thYear.Advertisements.services.AdvertisementsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/advertisements")
@RequiredArgsConstructor
public class AdvertisementsController {

    private final AdvertisementsService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdvertisementResponse> create(@Valid @ModelAttribute CreateAdvertisementRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping(value = "/{id}" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdvertisementResponse> update(@PathVariable Integer id, @Valid @ModelAttribute UpdateAdvertisementRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<AdvertisementResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
