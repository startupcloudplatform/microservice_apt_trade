package com.microservice.apttrade.api.dto;

import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ItemsResponse {

	@NonNull
	private String roadName;
	@NonNull
	private String aptName;
	@NonNull
	private String transactionAmount;
	@NonNull
	private int year;
	@NonNull
	private int month;
	@NonNull
	private double area;
	@NonNull
	private int floor;

}
