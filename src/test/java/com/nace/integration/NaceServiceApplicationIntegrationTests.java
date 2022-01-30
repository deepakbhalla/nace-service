package com.nace.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
class NaceServiceApplicationIntegrationTests {

    private static final String NACE_POST_API_ENDPOINT = "/nace/api/v1";
    private static final String NACE_GET_API_ENDPOINT = "/nace/api/v1/{order}";
    private static final String REQUEST_HEADER_FILE_PATH = "file-path";
    private static final String VALID_CSV_FILE_PATH = "NACE_REV2_20220129_073032.csv";

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testPutNaceDetailsShouldBeSuccessfullForCsvFile() throws Exception {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(REQUEST_HEADER_FILE_PATH, VALID_CSV_FILE_PATH);
        this.mockMvc.perform(post(NACE_POST_API_ENDPOINT).headers(httpHeaders)).andExpect(status().isOk());
    }

    @Test
    public void testGetNaceDetailsShouldBeSuccessfullForNoRecordFound() throws Exception {
        this.mockMvc.perform(get(NACE_GET_API_ENDPOINT, "12345")).andExpect(status().isNotFound());
    }

    @Test
    public void testGetNaceDetailsShouldBeSuccessfullForRecordFound() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(REQUEST_HEADER_FILE_PATH, VALID_CSV_FILE_PATH);
        this.mockMvc.perform(post(NACE_POST_API_ENDPOINT).headers(httpHeaders)).andReturn();
        this.mockMvc.perform(get(NACE_GET_API_ENDPOINT, "398857")).andExpect(status().isOk());
    }
}
