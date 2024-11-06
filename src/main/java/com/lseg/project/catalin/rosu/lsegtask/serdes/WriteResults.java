package com.lseg.project.catalin.rosu.lsegtask.serdes;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WriteResults {

    public static void writeDataAtOnce(String filePath, List<CsvRecord> data2Write)
    {
        File file = new File(filePath);
        try {
            java.io.Writer outputfile = new java.io.FileWriter(filePath);
            CSVWriter writer = new CSVWriter(outputfile);

            List<String[]> list = new ArrayList<>();
            data2Write.forEach(e -> list.add(new String[]{e.getStockId(), e.getTimeStamp().toString(), e.getStockPriceValue().toString()}));

            writer.writeAll(list);
            // closing writer connection
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
