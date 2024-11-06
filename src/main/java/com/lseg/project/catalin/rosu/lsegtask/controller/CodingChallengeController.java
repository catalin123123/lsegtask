package com.lseg.project.catalin.rosu.lsegtask.controller;

import com.lseg.project.catalin.rosu.lsegtask.BusinessLogic.AddPredictions;
import com.lseg.project.catalin.rosu.lsegtask.config.RestTemplateConfig;
import com.lseg.project.catalin.rosu.lsegtask.serdes.CsvRecord;
import com.lseg.project.catalin.rosu.lsegtask.serdes.WriteResults;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import java.io.*;
import java.util.stream.Collectors;

/**
 * This controller exposes 2 APIs/Functions
 * • 1st API/Function that, for each file provided, returns 10 consecutive data points starting from a random
 * timestamp.
 * • 2nd API/function that gets the output from 1st one and predicts the next 3 values in the timeseries data.
 */

@RestController
@Validated
public class CodingChallengeController {

    @Autowired
    RestTemplateConfig restTemplateConfig;

    private static final SimpleDateFormat stdDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    /**
     * 1st API/Function
     * @return
     * @throws IOException
     */
    @GetMapping("/1stAPI/Function")
    public Map<String, List<CsvRecord>> getTenRecords() throws IOException {

        File[] directories = new File("src//main//resources//static").listFiles(File::isDirectory);
        Map<String, List<CsvRecord>> allFiles = new HashMap<>();

        for (File dirs : directories) {
            for (File files : dirs.listFiles()) {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(getClass().getClassLoader().getResourceAsStream("static//" + dirs.getName() + "//" + files.getName())));

                CsvToBean<CsvRecord> theList = new CsvToBeanBuilder(bufferedReader).withType(CsvRecord.class).build();
                List<CsvRecord> parsedFromFileList = theList.parse();

                Random rand = new Random();
                int rand_int = rand.nextInt(parsedFromFileList.size() - 10);
                LocalDate randomDate = parsedFromFileList.get(rand_int).getTimeStamp();

                List<CsvRecord> first10RecordsAfterRandomDate = new ArrayList<>();
                for (LocalDate date = randomDate; date.isBefore(randomDate.plusDays(10)); date = date.plusDays(1)) {
                    for (CsvRecord r : parsedFromFileList) {
                        if (r.getTimeStamp().isEqual(date)) first10RecordsAfterRandomDate.add(r);
                    }
                }

                allFiles.put(dirs.getName() + "-" + files.getName(), first10RecordsAfterRandomDate);
            }
        }
        return allFiles;
    }

    /**
     * 2nd API/function
     * @param filesToBeSampled
     * @return
     * @throws IOException
     */
    @GetMapping("/2ndAPI/Function/{filesToBeSampled}")
    public String generateFiles(@PathVariable @Min(1) @Max(2) Integer filesToBeSampled) throws IOException {

        String url = "http://localhost:8080/1stAPI/Function";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Map<String, List<CsvRecord>>> entity = new HttpEntity<Map<String, List<CsvRecord>>>(headers);
        Map<String, List<LinkedHashMap>> responseFrom1stApi = restTemplateConfig.restTemplate().exchange(url, HttpMethod.GET, entity, HashMap.class).getBody();
        List<String> exchangeList = new ArrayList<>();
        for (Map.Entry<String, List<LinkedHashMap>> entry : responseFrom1stApi.entrySet()) {
            String exchange = entry.getKey().toString().substring(0, entry.getKey().toString().indexOf("-"));
            exchangeList.add(exchange);
            String instrument = entry.getKey().toString().substring(entry.getKey().toString().indexOf("-") + 1, entry.getKey().toString().indexOf("."));
            if (exchangeList.stream().filter(e -> e.equals(exchange)).count() <= filesToBeSampled.intValue()) {
                List<CsvRecord> initialData = entry.getValue().stream().map(e -> new CsvRecord(e.get("stockId").toString(), LocalDate.parse(e.get("timeStamp").toString()), Double.parseDouble(e.get("stockPriceValue").toString()))).collect(Collectors.toList());
                List<CsvRecord> enrichWithPredictionsData = AddPredictions.addPredictions(initialData);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                WriteResults.writeDataAtOnce("src//main//resources//results//" + exchange + "//"
                        + instrument + "-result"+ stdDateFormat.format(timestamp) +".csv", enrichWithPredictionsData);
            }
        }

        return responseFrom1stApi.toString();
    }

}
