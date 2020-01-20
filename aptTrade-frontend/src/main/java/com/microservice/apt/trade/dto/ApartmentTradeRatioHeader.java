package com.microservice.apt.trade.dto;

import com.microservice.apt.address.dto.AddressDetailData;
import lombok.*;

import java.util.List;

@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ApartmentTradeRatioHeader {

	@NonNull
	private String roadName;
	@NonNull
	private String apartment;

	private List<ApartmentTradeRatioBody> body;

}
