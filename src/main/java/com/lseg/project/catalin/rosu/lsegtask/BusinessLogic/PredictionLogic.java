package com.lseg.project.catalin.rosu.lsegtask.BusinessLogic;

import com.lseg.project.catalin.rosu.lsegtask.serdes.CsvRecord;

import java.util.List;

/**
 * If new prediction logic is added the new class will need implement this interface
 */
public interface PredictionLogic {
    public List<CsvRecord> addPredictions(List<CsvRecord> initialData) ;
}
