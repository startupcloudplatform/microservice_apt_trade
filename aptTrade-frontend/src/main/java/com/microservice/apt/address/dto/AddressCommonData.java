package com.microservice.apt.address.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressCommonData<T> {

    @JsonProperty("totalCount")
    private String totalCount;
    @JsonProperty("errorMessage")
    private String errorMessage;
    @JsonProperty("errorCode")
    private String errorCode;
    @JsonProperty("currentPage")
    private String currentPage;
    @JsonProperty("countPerPage")
    private String countPerPage;

}
