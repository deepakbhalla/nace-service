package com.nace.controllers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nace.builder.ApiResponseBuilder;
import com.nace.constants.NaceDetailsConstants;
import com.nace.dto.AddNaceDetails;
import com.nace.dto.GetNaceDetails;
import com.nace.entities.NaceDetailsEntity;
import com.nace.exceptions.EntityNotFoundException;
import com.nace.filters.RequestCorrelation;
import com.nace.services.NaceDetailsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Controller for Nace service.
 * 
 * @author Deepak Bhalla
 */
@RestController
@RequestMapping(NaceDetailsConstants.ENDPOINT_RESOURCE_NACE_V1)
@Validated
@Api(value = "Nace Details Service", description = "Operations pertaining to Nace details")
public class NaceDetailsController {

    private static final Logger LOG = LoggerFactory.getLogger(NaceDetailsController.class);

    @Autowired
    NaceDetailsService naceService;

    @Autowired
    ApiResponseBuilder apiResponseBuilder;

    /**
     * Imports Nace details from a CSV file to a database table.
     * 
     * @param filePath - String
     * @return ResponseEntity<AddNaceDetails>
     * @throws NumberFormatException
     * @throws IOException
     * @throws ConstraintViolationException
     */
    @ApiOperation(value = "Import Nace details from a CSV file to a database table.")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successful import of CSV file into database."),
            @ApiResponse(code = 400, message = "File types other than CSV are not allowed.\n"
                    + "Value of request header named 'file-path' cannot be empty.\n"
                    + "Value of request header named 'file-path' cannot be blank."),
            @ApiResponse(code = 500, message = "Something went wrong. Internal server error.") })
    @PostMapping
    public ResponseEntity<AddNaceDetails> putNaceDetails(
            @RequestHeader(name = NaceDetailsConstants.FILE_PATH) 
                @NotEmpty(message = "Value of request header named 'file-path' cannot be empty.") 
                @NotBlank(message = "Value of request header named 'file-path' cannot be blank.") 
                @Pattern(regexp = ".+(\\.csv)$", message = "File types other than CSV are not allowed.") 
            final String filePath)
            throws NumberFormatException, IOException, ConstraintViolationException {

        String corrId = RequestCorrelation.getId();
        LOG.info("[{}] NaceDetailsController | Import CSV | Start", corrId);
        List<NaceDetailsEntity> result = this.naceService.putNaceDetails(filePath.trim());
        ResponseEntity<AddNaceDetails> response = this.apiResponseBuilder.buildPostResponse(result);
        LOG.info("[{}] NaceDetailsController | Import CSV | End", corrId);
        return response;
    }

    /**
     * Retrieves the Nace details for an order number.
     * 
     * @param order - String
     * @return ResponseEntity<GetNaceDetails>
     * @throws EntityNotFoundException
     */
    @ApiOperation("Retrieve the Nace details for an order number.")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully fetched the nace details."),
            @ApiResponse(code = 400, message = "Order value must be a non-fraction integer.\n"
                    + "Order value must not be empty in the api request url.\n"
                    + "Order value must not be empty in the api request url.\n"
                    + "Order value must be greater than zero."),
            @ApiResponse(code = 500, message = "Something went wrong. Internal server error.") })
    @GetMapping(NaceDetailsConstants.ENDPOINT_GET_NACE_DTLS)
    public ResponseEntity<List<GetNaceDetails>> getNaceDetail (
            @PathVariable(name = "order", required = true) 
                @NotEmpty(message = "Order value must not be empty in the api request url.") 
                @NotBlank(message = "Order value must not be blank in the api request url.") 
                @Digits(message = "Order value must be a non-fraction integer.", fraction = 0, integer = 10) 
                @Min(value = 1, message = "Order value must be greater than zero.") 
            final String order)
            throws EntityNotFoundException {

        List<NaceDetailsEntity> result = this.naceService.getNaceDetailsByOrder(Long.valueOf(order));
        return this.apiResponseBuilder.buildGetResponse(result);
    }
}
