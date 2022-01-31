package com.nace.unit.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.springframework.test.util.ReflectionTestUtils;

import com.nace.entities.NaceDetailsEntity;
import com.nace.filters.RequestCorrelation;
import com.nace.repositories.NaceDetailsRepository;
import com.nace.services.NaceDetailsService;
import com.nace.services.executors.AddNaceDetailsExecutor;
import com.nace.utils.CsvFileReader;

@TestInstance(Lifecycle.PER_CLASS)
public class NaceDetailsServiceTest {

    @Autowired
    NaceDetailsService naceDetailsService;

    @Autowired
    NaceDetailsRepository naceDetailsRepository;

    @Autowired
    CsvFileReader csvFileReader;

    @Autowired
    AddNaceDetailsExecutor addNaceDetailsExecutor;

    private List<NaceDetailsEntity> persistedNaceDetails;

    @BeforeAll
    public void setUp() {

        RequestCorrelation.setId(UUID.randomUUID().toString().replaceAll("-", StringUtils.EMPTY));
        naceDetailsService = new NaceDetailsService();
        naceDetailsRepository = mock(NaceDetailsRepository.class);
        csvFileReader = mock(CsvFileReader.class);
        addNaceDetailsExecutor = mock(AddNaceDetailsExecutor.class);
        ReflectionTestUtils.setField(naceDetailsService, "csvFileReader", csvFileReader);
        ReflectionTestUtils.setField(naceDetailsService, "naceDetailsRepository", naceDetailsRepository);
        ReflectionTestUtils.setField(naceDetailsService, "addNaceDetailsExecutor", addNaceDetailsExecutor);
    }

    @Test
    public void testPutNaceDetailsShouldbeSuccessful()
            throws NumberFormatException, ConstraintViolationException, IOException, InterruptedException {

        givenControllerHasProvidedValidInputCsvFilePath();
        whenThePostOperationGetsInvoked();
        thenNaceDetailsReturnsResult();
    }

    @Test
    public void testPutNaceDetailShouldFailWhenFilePathIsNull()
            throws NumberFormatException, ConstraintViolationException, IOException {

        assertThrows(ConstraintViolationException.class, () -> naceDetailsService.putNaceDetails(null));
    }

    private void givenControllerHasProvidedValidInputCsvFilePath() throws InterruptedException {

        List<NaceDetailsEntity> addedNaceRecords = constructNaceDetailsEntityList();
        when(csvFileReader.readCSVFile(Mockito.anyString())).thenReturn(addedNaceRecords);
        when(addNaceDetailsExecutor.execute(addedNaceRecords, naceDetailsRepository)).thenReturn(addedNaceRecords);
    }

    private void whenThePostOperationGetsInvoked()
            throws NumberFormatException, ConstraintViolationException, IOException, InterruptedException {

        persistedNaceDetails = naceDetailsService.putNaceDetails("filePath.csv");
    }

    private void thenNaceDetailsReturnsResult() {

        assertEquals(2, persistedNaceDetails.size());
    }

    private List<NaceDetailsEntity> constructNaceDetailsEntityList() {
        List<NaceDetailsEntity> addedNaceRecords = new ArrayList<>();
        addedNaceRecords.add(NaceDetailsEntity.builder().code("11").description("description_1").id(1L)
                .itemAlsoIncludes("itemAlsoIncludes_1").itemIncludes("itemIncludes_1").itemExcludes("itemExcludes_1")
                .order(2222L).build());
        addedNaceRecords.add(NaceDetailsEntity.builder().code("11").description("description_2").id(1L)
                .itemAlsoIncludes("itemAlsoIncludes_2").itemIncludes("itemIncludes_2").itemExcludes("itemExcludes_2")
                .order(1111L).build());
        return addedNaceRecords;
    }
}
