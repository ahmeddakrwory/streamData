package com.example.nour.stream.demonourjpa.controllers;

import com.example.nour.stream.demonourjpa.entity.Region;
import com.example.nour.stream.demonourjpa.repos.CsvWriterService;
import com.example.nour.stream.demonourjpa.repos.RegionRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class RejonController {
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private  CsvWriterService csvWriterService;


    //application/stream+json"
    @GetMapping(value = "/all", produces = {"application/stream+json"})
    public Flux<Region> gitall() {
        return Flux.create(regionFluxSink -> {
            for (Region r : regionRepository.findAll()) {
                regionFluxSink.next(r);
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            regionFluxSink.complete();
        });
    }

    @GetMapping(value = "/download")
    @ResponseBody
    public ResponseEntity<Mono<Resource>> downloadCsv() {
        String fileName = String.format("%s.csv", RandomStringUtils.randomAlphabetic(10));
       csvWriterService.setAll((List<Region>) regionRepository.findAll());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(csvWriterService.generateCsv()
                        .flatMap(x -> {
                            Resource resource = new InputStreamResource(x);

                            return Mono.just(resource);
                        }));
    }
    @GetMapping("/allBulck")
    public List<Region>getregions(){
        return  regionRepository.findAll();
    }
}
