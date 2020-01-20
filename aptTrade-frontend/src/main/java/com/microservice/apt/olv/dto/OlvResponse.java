package com.microservice.apt.olv.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OlvResponse {
	private String dong;
	private String name;
	private int stdYear;
	private int unitArValue;
}
