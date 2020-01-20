package com.microservice.apt.trade.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApartmentTradeResponse {

    @ApiModelProperty("도로명")
    @JsonProperty("roadName")
    private String roadName;

    @ApiModelProperty("아파트명")
    @JsonProperty("aptName")
    private String aptName;

    @ApiModelProperty("실거래가")
    @JsonProperty("transactionAmount")
    private String transactionAmount;

    @ApiModelProperty("거래년도")
    @JsonProperty("year")
    private int year;

    @ApiModelProperty("거래월")
    @JsonProperty("month")
    private int month;

    @ApiModelProperty("전용면적")
    @JsonProperty("area")
    private double area;

    @ApiModelProperty("층")
    @JsonProperty("floor")
    private int floor;

}
