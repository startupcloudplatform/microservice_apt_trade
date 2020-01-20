package com.microservice.apt.trade.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DeclaredValueResponse {

    @ApiModelProperty("아파트명")
    private String name;
    @ApiModelProperty("아파트 동")
    private String dong;
    @ApiModelProperty("")
    private int stgYear;
    @ApiModelProperty("위도")
    private double latitude;
    @ApiModelProperty("경도")
    private double longitude;
    @ApiModelProperty("단위 면적당 가격")
    private long   unitArValue;
}
