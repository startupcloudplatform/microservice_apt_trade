package com.microservice.apttrade.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AptTradeBodyResponse<T> {

    @JsonProperty("pageNo")
    private int pageNo;

    @JsonProperty("totalCount")
    private int totalCount;

    @JsonProperty("numOfRows")
    private int numOfRows;

}
