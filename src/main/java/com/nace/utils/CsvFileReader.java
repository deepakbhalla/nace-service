package com.nace.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.nace.entities.NaceDetailsEntity;
import com.nace.filters.RequestCorrelation;
import com.opencsv.CSVReader;

/**
 * CSV File Reader.
 * 
 * @author Deepak Bhalla
 *
 */
@Component
public class CsvFileReader {

    private static final Logger LOG = LoggerFactory.getLogger(CsvFileReader.class);

    /**
     * Parser for CSV file from the given path.
     * 
     * @param filePath - Input CSV file path
     * @return naceDetails - List of type NaceDetailsEntity.
     */
    public List<NaceDetailsEntity> readCSVFile(String filePath) {
        List<NaceDetailsEntity> naceDetails = new ArrayList<>();

        String corrId = RequestCorrelation.getId();
        LOG.info("[{}] CsvFileReader | Read CSV | Start", corrId);
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] record;
            int row = 0;
            while ((record = reader.readNext()) != null) {

                if (row > 0) {
                    NaceDetailsEntity nace = buildNaceExcelDetails(record);
                    if (!Objects.isNull(nace)) {
                        naceDetails.add(nace);
                    }
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOG.info("[{}] CsvFileReader | Read CSV | Exception Occurred | End", corrId);
        }
        LOG.info("[{}] CsvFileReader | Read CSV | End", corrId);
        return naceDetails;
    }

    private NaceDetailsEntity buildNaceExcelDetails(String[] record) {

        NaceDetailsEntity nace = NaceDetailsEntity.builder().order(Long.valueOf(getDefaultValue(record[0])))
                .level(Long.valueOf(getDefaultValue(record[1]))).code(getDefaultValue(record[2]))
                .parent(getDefaultValue(record[3])).description(getDefaultValue(record[4]))
                .itemIncludes(getDefaultValue(record[5])).itemAlsoIncludes(getDefaultValue(record[6]))
                .rulings(getDefaultValue(record[7])).itemExcludes(getDefaultValue(record[8]))
                .referencesIsic(getDefaultValue(record[9])).build();
        return nace;
    }

    private String getDefaultValue(String record) {
        return StringUtils.defaultString(record, StringUtils.EMPTY);
    }
}
