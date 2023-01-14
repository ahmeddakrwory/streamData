package com.example.nour.stream.demonourjpa.repos;

import com.example.nour.stream.demonourjpa.entity.Region;
import com.example.nour.stream.demonourjpa.utils.ByteArrayInOutStream;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

@Service
public class CsvWriterService {
//    @Autowired
//    private RegionRepository regionRepository;
    private  List<Region> all;

    public List<Region> getAll() {
        return all;
    }

    public void setAll(List<Region> all) {
        this.all = all;
    }

    public Mono<ByteArrayInputStream> generateCsv(){
        String[] columns = {"regionName", "id"};

//        List<Region>all=regionRepository.findAll();
        return Mono.fromCallable(() -> {
            try {
                ByteArrayInOutStream stream = new ByteArrayInOutStream();
                OutputStreamWriter streamWriter = new OutputStreamWriter(stream);
                CSVWriter writer = new CSVWriter(streamWriter);

                ColumnPositionMappingStrategy mappingStrategy = new ColumnPositionMappingStrategy();
                mappingStrategy.setType(Region.class);
                mappingStrategy.setColumnMapping(columns);
                writer.writeNext(columns);

                StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                        .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                        .withMappingStrategy(mappingStrategy)
                        .withSeparator(',')
                        .build();

                beanToCsv.write(getAll());
                streamWriter.flush();
                return stream.getInputStream();
            }
            catch (CsvException | IOException e) {
                throw new RuntimeException(e);
            }

        }).subscribeOn(Schedulers.boundedElastic());

    }
}
