package com.microservice.apt.trade.controller;

import com.microservice.apt.trade.dto.ApartmentTradeRatioHeader;
import com.microservice.apt.trade.service.AptTradeApiService;
import com.microservice.apt.util.DataException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/trade")
@RestController
public class AptTradeApiController {

	@Autowired
	private AptTradeApiService aptTradeApiService;

	@ApiOperation(value = "당해연도 분기 별 아파트 실거래가 대비 공시지가 비율 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "rnMgtSn",  value = "도로명 코드", required = true, dataType = "string", paramType ="path"),
			@ApiImplicitParam(name = "pnu",      value = "PNU(지번)", required = true, dataType = "string", paramType ="query"),
			@ApiImplicitParam(name = "buldMnnm", value = "도로명 건물 본번호 코드", required = true, dataType = "long", paramType = "query"),
			@ApiImplicitParam(name = "buldSlno", value = "도로명 건물 부번호 코드", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "quarter",  value = "분기",      required = true, dataType = "int", paramType = "query", defaultValue = "1")
	})
	@RequestMapping(value = "/list/{rnMgtSn}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public List<ApartmentTradeRatioHeader> listByAmdCodeAndYear(@PathVariable(name = "rnMgtSn")  String rnMgtSn,
																@RequestParam(name = "pnu") String pnu,
																@RequestParam(name = "buldMnnm") long buldMnnm,
																@RequestParam(name = "buldSlno") long buldSlno,
																@RequestParam(defaultValue = "1", required = false) int quarter){

		return aptTradeApiService.listByAddrCodeAndYear(rnMgtSn, pnu, buldMnnm, buldSlno, quarter);
	}


	@ExceptionHandler(DataException.DefaultException.class )
	@ApiOperation(value = "당해연도 분기 및 행정구역 별 아파트 실거래가 대비 공시지가 비율 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "address",    value = "행정구역",     required = true, dataType = "string", paramType ="query"),
			@ApiImplicitParam(name = "priceRange", value = "실거래가 범위(ex:30000-60000)", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "areaRange",  value = "전용면적 범위(ex:30-60)",       required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "quarter",    value = "분기",        required = true, dataType = "int", paramType = "query", defaultValue = "1")
	})
	@RequestMapping(value = "/condition/list", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public List<ApartmentTradeRatioHeader> listByAddressAndQuarter(@RequestParam(name= "address")    String address,
																   @RequestParam(name= "priceRange", required = false) String priceRange,
																   @RequestParam(name= "areaRange",  required = false) String areaRange,
																   @RequestParam(defaultValue = "1", required = false) int quarter) {

		return aptTradeApiService.listByAddressByCondition(address, priceRange, areaRange, quarter);
	}

}
