package com.microservice.apt.address.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AddressDTO {
    private long aId;
    private String sido;
    private String gugun;
    private String dong;
    private String code;


}
