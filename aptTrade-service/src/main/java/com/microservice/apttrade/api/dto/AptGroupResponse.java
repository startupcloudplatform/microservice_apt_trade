package com.microservice.apttrade.api.dto;

import lombok.*;

import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AptGroupResponse {

	@NonNull
	private String aptName;
	@NonNull
	private List<AptTradeResponse> items;

}
