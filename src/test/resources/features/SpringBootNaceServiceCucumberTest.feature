@NaceDetailsBDDTests
Feature: Spring Boot Nace Details Service
  I want to verify springboot nace details service using Cucumber

  @CheckServiceHealth
  Scenario: Check Nace Service Health Status
    Given I send a request to the URL "/nace/api/v1/health" to check the service health
    Then the response will return status 200

  @ImportCsvNaceDetailsIntoDatabase
  Scenario: Import Nace Details CSV into Application Database
    Given I send a request to the URL "/nace/api/v1" with CSV file name "NACE_REV2_20220129_073032.csv" and to put nace csv data to application database
    Then the response will return status 200
    
  @ImportTxtNaceDetailsIntoDatabase
  Scenario: Import Nace Details TXT file into Application Database
    Given I send a request to the URL "/nace/api/v1" with TXT file name "NACE_REV2_20220129_073032.txt" to put nace txt data to application database
    Then the response will return status 400

  @ImportEmptyFileNameInRequestHeaderNaceDetailsIntoDatabase
  Scenario: Import Nace Details with EMPTY file name into Application Database
    Given I send a request to the URL "/nace/api/v1" with EMPTY "" file name
    Then the response will return status 400

  @ImportNoFileNameNaceDetailsIntoDatabase
  Scenario: Import Nace Details with NO file name into Application Database
    Given I send a request to the URL "/nace/api/v1" with NO file name
    Then the response will return status 400

  @GetNaceDetailsWithBlankOrderInUrlPath
  Scenario: Get Nace details with blank order in the API Url path
    Given I send a request to the URL "/nace/api/v1/{order}" with blank " " order to get Nace details
    Then the response will return status 400

  @GetNaceDetailsWithBlankOrderInUrlPath
  Scenario: Get Nace details with negative order number in the API Url path
    Given I send a request to the URL "/nace/api/v1/{order}" with negative "-120" order number to get Nace details
    Then the response will return status 400
