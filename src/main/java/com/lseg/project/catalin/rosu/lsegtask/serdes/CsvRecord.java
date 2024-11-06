package com.lseg.project.catalin.rosu.lsegtask.serdes;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;

import java.time.LocalDate;

public class CsvRecord {

    @CsvBindByPosition(position = 0)
    private String stockId;

    @CsvCustomBindByPosition(position = 1, converter = LocalDateConverter.class)
    private LocalDate timeStamp;

    @CsvBindByPosition(position = 2)
    private Double stockPriceValue;

    public CsvRecord(String stockId, LocalDate timeStamp, Double stockPriceValue) {
        this.stockId = stockId;
        this.timeStamp = timeStamp;
        this.stockPriceValue = stockPriceValue;
    }

    public CsvRecord() {
    }


    public String getStockId() {
        return stockId;
    }

    public LocalDate getTimeStamp() {
        return timeStamp;
    }

    public Double getStockPriceValue() {
        return stockPriceValue;
    }


    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public void setTimeStamp(LocalDate timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setStockPriceValue(Double stockPriceValue) {
        this.stockPriceValue = stockPriceValue;
    }
}
