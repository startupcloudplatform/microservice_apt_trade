package com.microservice.apttrade.address.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressData {
    @JsonProperty("common")
    private AddressCommonData common;

    @JsonProperty("juso")
    private List<AddressDetailData> juso;

}
