package com.nace.services;

import java.io.IOException;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.nace.constants.NaceDetailsConstants;
import com.nace.entities.NaceDetailsEntity;
import com.nace.filters.RequestCorrelation;
import com.nace.repositories.NaceDetailsRepository;
import com.nace.utils.CsvFileReader;

/**
 * Nace Details Service Implementation.
 * 
 * @author Deepak Bhalla
 *
 */
@Service
public class NaceDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(NaceDetailsService.class);

    @Autowired
    private NaceDetailsRepository naceDetailsRepository;

    @Autowired
    CsvFileReader csvFileReader;

    /**
     * Imports Nace Details CSV file into Database table.
     * 
     * @param filePath - Path of the CSV file to be imported.
     * @return result - List of type NaceDetailsEntity
     * @throws NumberFormatException
     * @throws IOException
     * @throws ConstraintViolationException
     */
    public List<NaceDetailsEntity> putNaceDetails(String filePath)
            throws NumberFormatException, IOException, ConstraintViolationException {

        String corrId = RequestCorrelation.getId();
        LOG.info("[{}] NaceDetailsService | Import CSV | Start", corrId);
        if (StringUtils.isEmpty(filePath)) {
            throw new ConstraintViolationException("Missing request header named 'file-path'.", null);
        }

        /** Convert CSV file to Java Object **/
        List<NaceDetailsEntity> naceDetails = csvFileReader.readCSVFile(filePath);

        /** Persist Java Object to database **/
        List<NaceDetailsEntity> result = naceDetailsRepository.saveAll(naceDetails);
        LOG.info("[{}] NaceDetailsService | Import CSV | Start", corrId);
        return result;
    }

    /**
     * Finds Nace details for a given order. Results are cached for future
     * invocations within cache's ttl.
     * 
     * @param order - order number for which nace details have been requested.
     * @return List of type NaceDetailsEntity
     */
    @Cacheable(cacheNames = NaceDetailsConstants.NACE_CACHE_NAME, key = "#order")
    public List<NaceDetailsEntity> getNaceDetailsByOrder(Long order) {
        LOG.info("No entry found in cache, fetching it from the database for order: {}", order);
        return naceDetailsRepository.findByOrder(order);
    }
}
