package com.example.nour.stream.demonourjpa.controllers;

import com.example.nour.stream.demonourjpa.entity.Region;
import com.example.nour.stream.demonourjpa.repos.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class RejonController {
    @Autowired
    private RegionRepository regionRepository;
    @GetMapping(value = "/all",produces = {"application/stream+json"})
    public Flux<Region> gitall(){
        return Flux.create(regionFluxSink -> {
            for ( Region r:regionRepository.findAll() ) {
                regionFluxSink.next(r);
                try {
                    Thread.sleep(500);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            regionFluxSink.complete();
        });
    }
}
