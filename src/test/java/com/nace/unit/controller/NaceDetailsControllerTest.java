package com.nace.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.nace.builder.ApiResponseBuilder;
import com.nace.constants.NaceEnum;
import com.nace.controllers.NaceDetailsController;
import com.nace.dto.AddedNaceDetailsDto;
import com.nace.dto.GetNaceDetailsDto;
import com.nace.entities.NaceDetailsEntity;
import com.nace.exceptions.EntityNotFoundException;
import com.nace.filters.RequestCorrelation;
import com.nace.services.NaceDetailsService;

@WebMvcTest
@TestInstance(Lifecycle.PER_CLASS)
public class NaceDetailsControllerTest {

    private static final String NACE_POST_API_ENDPOINT = "/nace/api/v1";
    private static final String NACE_GET_API_ENDPOINT = "/nace/api/v1/{order}";
    private static final String REQUEST_HEADER_FILE_PATH = "file-path";
    private static final String VALID_CSV_FILE_PATH = "NACE_REV2_20220129_073032.csv";
    private static final String INVALID_FILE_PATH = "filePath.txt";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    NaceDetailsController naceDetailsController;

    @MockBean
    NaceDetailsService mockNaceService;

    @MockBean
    ApiResponseBuilder apiResponseBuilder;

    private MvcResult mvcResult;

    @BeforeAll
    public void setUp() {

        RequestCorrelation.setId(UUID.randomUUID().toString().replaceAll("-", StringUtils.EMPTY));
        naceDetailsController = new NaceDetailsController();
        mockNaceService = mock(NaceDetailsService.class);
        apiResponseBuilder = mock(ApiResponseBuilder.class);
        ReflectionTestUtils.setField(naceDetailsController, "naceService", mockNaceService);
        ReflectionTestUtils.setField(naceDetailsController, "apiResponseBuilder", apiResponseBuilder);
    }

    @Test
    public void testPutNaceDetailsShouldBeSuccessful() throws Exception {

        givenUserHasProvidedValidInputCsvFilePath();
        whenTheUserImportsTheCsvFile();
        thenNaceDetailsReturnsResult();
    }

    @Test
    public void testGetNaceDetailsShouldBeSuccessful() throws Exception {

        givenUserHasProvidedValidOrderInTheAPIUrlPath();
        whenTheApiOperationGetsInvoked();
        thenGetNaceDetailsForOrder();
    }

    @Test
    public void testPutNaceDetailsShouldFailForNonCsvFileExtn() throws Exception {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(REQUEST_HEADER_FILE_PATH, INVALID_FILE_PATH);
        this.mockMvc.perform(post(NACE_POST_API_ENDPOINT).headers(httpHeaders)).andExpect(status().isBadRequest());
    }

    @Test
    public void testPutNaceDetailsShouldFailForEmptyCsvFileName() throws Exception {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(REQUEST_HEADER_FILE_PATH, StringUtils.EMPTY);
        this.mockMvc.perform(post(NACE_POST_API_ENDPOINT).headers(httpHeaders)).andExpect(status().isBadRequest());
    }

    @Test
    public void testPutNaceDetailsShouldFailForMissingFilePathRequestHeader() throws Exception {

        HttpHeaders httpHeaders = new HttpHeaders();
        this.mockMvc.perform(post(NACE_POST_API_ENDPOINT).headers(httpHeaders)).andExpect(status().isBadRequest());
    }

    @Test
    public void testGetNaceDetailsShouldFailForEmptyOrder() throws Exception {
        this.mockMvc.perform(get(NACE_GET_API_ENDPOINT, StringUtils.EMPTY)).andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void testGetNaceDetailsShouldFailForBlankOrder() throws Exception {
        this.mockMvc.perform(get(NACE_GET_API_ENDPOINT, StringUtils.SPACE)).andExpect(status().isBadRequest());
    }

    @Test
    public void testGetNaceDetailsShouldFailForFractionNumberOrder() throws Exception {
        this.mockMvc.perform(get(NACE_GET_API_ENDPOINT, "1.0")).andExpect(status().isBadRequest());
    }

    @Test
    public void testGetNaceDetailsShouldFailForZeroNumberedOrder() throws Exception {
        this.mockMvc.perform(get(NACE_GET_API_ENDPOINT, "0")).andExpect(status().isBadRequest());
    }

    private void givenUserHasProvidedValidInputCsvFilePath()
            throws NumberFormatException, ConstraintViolationException, IOException, InterruptedException {

        List<NaceDetailsEntity> addedNaceRecords = constructNaceDetailsEntityList();
        when(this.mockNaceService.putNaceDetails(Mockito.anyString())).thenReturn(addedNaceRecords);

        ResponseEntity<AddedNaceDetailsDto> postSuccessResponse = constructPostSuccessResponse(addedNaceRecords);
        when(apiResponseBuilder.buildPostResponse(addedNaceRecords)).thenReturn(postSuccessResponse);
    }

    private void whenTheUserImportsTheCsvFile() throws Exception {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(REQUEST_HEADER_FILE_PATH, VALID_CSV_FILE_PATH);
        mvcResult = this.mockMvc.perform(post(NACE_POST_API_ENDPOINT).headers(httpHeaders)).andReturn();
    }

    private void thenNaceDetailsReturnsResult() {

        assertEquals(mvcResult.getResponse().getStatus(), HttpStatus.OK.value());
    }

    private void givenUserHasProvidedValidOrderInTheAPIUrlPath() throws EntityNotFoundException {

        List<NaceDetailsEntity> fetchedEntities = constructFetchedNaceDetailsEntityList();
        when(this.mockNaceService.getNaceDetailsByOrder(Mockito.anyLong())).thenReturn(fetchedEntities);

        ResponseEntity<List<GetNaceDetailsDto>> fetchedResponseDto = constructGetSuccessResponse(fetchedEntities);
        when(this.apiResponseBuilder.buildGetResponse(fetchedEntities)).thenReturn(fetchedResponseDto);
    }

    private void whenTheApiOperationGetsInvoked() throws Exception {

        mvcResult = this.mockMvc.perform(get(NACE_GET_API_ENDPOINT, "398857")).andReturn();
    }

    private void thenGetNaceDetailsForOrder() {

        assertEquals(mvcResult.getResponse().getStatus(), HttpStatus.OK.value());
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

    private ResponseEntity<List<GetNaceDetailsDto>> constructGetSuccessResponse(List<NaceDetailsEntity> resultArg) {

        List<GetNaceDetailsDto> fetchedNaceRecords = new ArrayList<>();
        resultArg.stream().forEach(result -> {
            fetchedNaceRecords.add(GetNaceDetailsDto.builder().order(result.getOrder()).level(result.getLevel())
                    .code(result.getCode()).parent(result.getParent()).description(result.getDescription())
                    .itemIncludes(result.getItemIncludes()).itemAlsoIncludes(result.getItemAlsoIncludes())
                    .rulings(result.getRulings()).itemExcludes(result.getItemExcludes())
                    .referencesIsic(result.getReferencesIsic()).build());
        });

        return new ResponseEntity<List<GetNaceDetailsDto>>(fetchedNaceRecords, HttpStatus.OK);
    }

    private List<NaceDetailsEntity> constructFetchedNaceDetailsEntityList() {
        List<NaceDetailsEntity> fetchedNaceRecords = new ArrayList<>();
        fetchedNaceRecords.add(NaceDetailsEntity.builder().code("11").description("description_1").id(1L)
                .itemAlsoIncludes("itemAlsoIncludes_1").itemIncludes("itemIncludes_1").itemExcludes("itemExcludes_1")
                .order(398857L).build());
        return fetchedNaceRecords;
    }

    private ResponseEntity<AddedNaceDetailsDto> constructPostSuccessResponse(List<NaceDetailsEntity> result) {
        AddedNaceDetailsDto returnDto = AddedNaceDetailsDto.builder().status(NaceEnum.STATUS_SUCCESS.getValue())
                .details(Arrays.asList(NaceEnum.ADD_RECORDS_SUCCESS_MSG.getValue()
                        .concat(StringUtils.defaultString(String.valueOf(result.size()), NaceEnum.ZERO.getValue())
                                .concat(NaceEnum.RECORDS_MSG_TXT.getValue()))))
                .timestamp(LocalDateTime.now()).build();

        return new ResponseEntity<AddedNaceDetailsDto>(returnDto, HttpStatus.OK);
    }
}
