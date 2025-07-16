package com.graduationProject._thYear.Advertisements.services;

import com.graduationProject._thYear.Advertisements.dtos.requests.CreateAdvertisementRequest;
import com.graduationProject._thYear.Advertisements.dtos.requests.UpdateAdvertisementRequest;
import com.graduationProject._thYear.Advertisements.dtos.responses.AdvertisementResponse;
import com.graduationProject._thYear.Advertisements.models.Advertisements;
import com.graduationProject._thYear.Advertisements.repositories.AdvertisementsRepository;
import com.graduationProject._thYear.Product.models.Product;
import com.graduationProject._thYear.Product.services.ImageStorageService;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvertisementsService {
    private final AdvertisementsRepository repo;
    private final ImageStorageService imageStorageService;


    public AdvertisementResponse create(CreateAdvertisementRequest request) {

        String tempImagePath = null;
        try {
            if (request.getMediaUrl() == null || request.getMediaUrl().isEmpty()) {
                throw new IllegalArgumentException("Media file is required.");
            }

            tempImagePath = imageStorageService.saveToTemp(request.getMediaUrl());
            String permanentImagePath = imageStorageService.moveToPermanent(tempImagePath);

            Advertisements adv = Advertisements.builder()
                    .name(request.getName())
                    .duration(request.getDuration())
                    .mediaUrl(permanentImagePath)
                    .build();

            Advertisements saved = repo.save(adv);
            return toResponse(saved);

        } catch (Exception e) {
            if (tempImagePath != null) {
                imageStorageService.deleteTemp(tempImagePath);
            }
            throw e;
        }
    }


    public AdvertisementResponse update(Integer id, UpdateAdvertisementRequest request) {
        Advertisements adv = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Advertisement not found"));

        String tempImagePath = null;
        try {
            if (request.getName() != null) adv.setName(request.getName());
            if (request.getDuration() != null) adv.setDuration(request.getDuration());

            repo.save(adv);

            if (request.getMediaUrl() != null && !request.getMediaUrl().isEmpty()) {
                tempImagePath = imageStorageService.saveToTemp(request.getMediaUrl());

                if (adv.getMediaUrl() != null) {
                    imageStorageService.deletePermanent(adv.getMediaUrl());
                }

                String finalPath = imageStorageService.moveToPermanent(tempImagePath);
                adv.setMediaUrl(finalPath);
            }

            Advertisements updated = repo.save(adv);
            return toResponse(updated);
        } catch (Exception ex) {
            if (tempImagePath != null) imageStorageService.deleteTemp(tempImagePath);
            throw ex;
        }
    }



    public void delete(Integer id) {
        Advertisements adv = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Advertisement not found"));
        repo.delete(adv);
    }


    public AdvertisementResponse getById(Integer id) {
        return repo.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Advertisement not found"));
    }


    public List<AdvertisementResponse> getAll() {
        return repo.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private AdvertisementResponse toResponse(Advertisements adv) {
        return AdvertisementResponse.builder()
                .id(adv.getId())
                .name(adv.getName())
                .mediaUrl(adv.getMediaUrl())
                .duration(adv.getDuration())
                .build();
    }
}
