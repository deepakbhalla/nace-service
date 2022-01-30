package com.nace.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * Represents retrieved nace details.
 * 
 * @author Deepak Bhalla
 *
 */
@Data
@Builder
public class GetNaceDetailsDto implements Serializable {

    private static final long serialVersionUID = 8479298145284697904L;

    private Long order;
    private Long level;
    private String code;
    private String parent;
    private String description;
    private String itemIncludes;
    private String itemAlsoIncludes;
    private String rulings;
    private String itemExcludes;
    private String referencesIsic;
}
