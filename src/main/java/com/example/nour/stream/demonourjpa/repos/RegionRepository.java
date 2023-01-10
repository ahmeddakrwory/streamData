package com.example.nour.stream.demonourjpa.repos;

import com.example.nour.stream.demonourjpa.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
}