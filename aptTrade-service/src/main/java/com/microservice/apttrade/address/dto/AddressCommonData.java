package com.microservice.apttrade.address.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressCommonData<T> {

    @JsonProperty("totalCount")
    private String totalCount;

    @JsonProperty("errorMessage")
    private String message;

    @JsonProperty("errorCode")
    private String code;

    @JsonProperty("currentPage")
    private String currentPage;

    @JsonProperty("countPerPage")
    private String countPerPage;



}
