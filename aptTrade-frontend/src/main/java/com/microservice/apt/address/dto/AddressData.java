package com.microservice.apt.address.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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
