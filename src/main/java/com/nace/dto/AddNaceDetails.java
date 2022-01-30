package com.nace.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * Represents persisted nace details.
 * 
 * @author Deepak Bhalla
 *
 */
@Data
@Builder
public class AddNaceDetails implements Serializable {

    private static final long serialVersionUID = -8211588207894834628L;

    private String status;
    private List<String> details;
    private LocalDateTime timestamp;
}
