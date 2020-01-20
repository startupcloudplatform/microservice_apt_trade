package com.microservice.apttrade.api.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AptTradeResponse {

    private AptTradeBodyResponse body = new AptTradeBodyResponse();
    private List<AptTradeItemsResponse> items = new ArrayList<>();

}
