package com.nace.bdd;

import static org.hamcrest.CoreMatchers.equalTo;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringbootCucumberTestDefinitions {

    private final static String BASE_URI = "http://localhost";

    @LocalServerPort
    private int port;

    private ValidatableResponse validatableResponse;

    private void configureRestAssured() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
    }

    protected RequestSpecification requestSpecification() {
        configureRestAssured();
        return RestAssured.given();
    }

    @Given("I send a request to the URL {string} to check the service health")
    public void iSendARequestForHealthCheck(String endpoint) throws Throwable {
        validatableResponse = requestSpecification().contentType(ContentType.JSON).when().get(endpoint).then();
    }

    @Given("I send a request to the URL {string} with CSV file name {string} and to put nace csv data to application database")
    public void iSendARequestToPostCSVFile(String endpoint, String fileName) throws Throwable {
        validatableResponse = requestSpecification().contentType(ContentType.JSON).header(new Header("file-path", fileName)).when().post(endpoint)
                .then();
    }

    @Given("I send a request to the URL {string} with TXT file name {string} to put nace txt data to application database")
    public void iSendARequestToPostTxtFile(String endpoint, String fileName) throws Throwable {
        validatableResponse = requestSpecification().contentType(ContentType.JSON).header(new Header("file-path", fileName)).when().post(endpoint)
                .then();
    }

    @Given("I send a request to the URL {string} with EMPTY {string} file name")
    public void iSendARequestToPostEmptyFileName(String endpoint, String fileName) throws Throwable {
        validatableResponse = requestSpecification().contentType(ContentType.JSON).header(new Header("file-path", fileName)).when().post(endpoint)
                .then();
    }

    @Given("I send a request to the URL {string} with NO file name")
    public void iSendARequestToPostEmptyFileName(String endpoint) throws Throwable {
        validatableResponse = requestSpecification().contentType(ContentType.JSON).when().post(endpoint).then();
    }

    @Then("the response will return status {int}")
    public void extractResponseForHealthCheck(int status) {
        validatableResponse.assertThat().statusCode(equalTo(status));
    }

    @Given("I send a request to the URL {string} with blank {string} order to get Nace details")
    public void iSendARequestToGetNaceDetailsWithBlankOrderInPath(String endpoint, String order) throws Throwable {
        validatableResponse = requestSpecification().contentType(ContentType.JSON).pathParam("order", order).when().get(endpoint).then();
    }

    @Given("I send a request to the URL {string} with negative {string} order number to get Nace details")
    public void iSendARequestToGetNaceDetailsWithNegativeOrderInPath(String endpoint, String order) throws Throwable {
        validatableResponse = requestSpecification().contentType(ContentType.JSON).pathParam("order", order).when().get(endpoint).then();
    }
}