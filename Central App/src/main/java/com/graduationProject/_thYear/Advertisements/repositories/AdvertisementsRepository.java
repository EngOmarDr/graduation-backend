package com.graduationProject._thYear.Advertisements.repositories;

import com.graduationProject._thYear.Advertisements.models.Advertisements;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementsRepository extends JpaRepository<Advertisements, Integer> {
}
