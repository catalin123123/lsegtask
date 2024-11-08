package com.lseg.project.catalin.rosu.lsegtask.BusinessLogic;

import com.lseg.project.catalin.rosu.lsegtask.serdes.CsvRecord;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Takes a list of CsvRecord type and adds predictions to it using the logic provided in the requirement
 */
@Component
public class AddPredictionsUsingProvidedLogic implements PredictionLogic {
    public List<CsvRecord> addPredictions(List<CsvRecord> initialData) {
        double secondHighestValuePresent = initialData.stream().map(m -> m.getStockPriceValue()).sorted(Comparator.reverseOrder()).skip(1).findFirst().get();
        double halfTheDiffBetweenNAndNPlus1 = (initialData.get(initialData.size() - 1).getStockPriceValue() - secondHighestValuePresent) / 2;
        double lastElement = (secondHighestValuePresent - halfTheDiffBetweenNAndNPlus1) / 4;

        List<CsvRecord> enrichedList = new ArrayList<>(initialData);

        enrichedList.add(new CsvRecord(initialData.get(1).getStockId(), initialData.get(initialData.size() - 1).getTimeStamp().plusDays(1), secondHighestValuePresent));
        enrichedList.add(new CsvRecord(initialData.get(1).getStockId(), initialData.get(initialData.size() - 1).getTimeStamp().plusDays(1), halfTheDiffBetweenNAndNPlus1));
        enrichedList.add(new CsvRecord(initialData.get(1).getStockId(), initialData.get(initialData.size() - 1).getTimeStamp().plusDays(1), lastElement));

        return enrichedList;
    }
}