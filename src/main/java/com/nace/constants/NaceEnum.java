package com.nace.constants;

/**
 * Application Enum.
 * 
 * @author Deepak Bhalla
 *
 */
public enum NaceEnum {

    ADD_RECORDS_SUCCESS_MSG("Successfully imported CSV file. CSV rows count: "),
    ADD_RECORDS_FAILURE_MSG("Successfully imported CSV file. CSV rows count: "),
    RECORDS_MSG_TXT(" records."), 
    STATUS_SUCCESS("SUCCESS"),
    STATUS_FAILED("FAILED"),
    ZERO("0");

    private String value;

    private NaceEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
    
    public enum Errors {
        
        BAD_REQUEST("BAD_REQUEST"),
        NO_RECORD_FOUND("NO_RECORD_FOUND"),
        INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR"),
        NO_RECORD_FOUND_MSG("No record found against the given order number."),
        INTERNAL_SERVER_ERROR_MSG("Something went wrong. Internal server error occurred. Please try later.");

        private String value;
        
        private Errors(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return this.value;
        }
    }
}
