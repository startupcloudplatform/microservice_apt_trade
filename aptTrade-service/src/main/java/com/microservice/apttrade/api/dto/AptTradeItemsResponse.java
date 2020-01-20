package com.microservice.apttrade.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@JsonTypeName("item")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AptTradeItemsResponse {

	@JsonProperty("도로명")
	private String roadName;

	@JsonProperty("아파트")
	private String aptName;

	@JsonProperty("거래금액")
	private String transactionAmount;

	@JsonProperty("년")
	private int year;

	@JsonProperty("월")
	private int month;

	@JsonProperty("전용면적")
	private double area;

	@JsonProperty("층")
	private int floor;

	@JsonProperty("도로명코드")
	private long rnMgtSn;

	@JsonProperty("도로명건물본번호코드")
	private long buldMnnm;

	@JsonProperty("도로명건물부번호코드")
	private long buldSlno;

}
