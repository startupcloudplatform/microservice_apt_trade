package com.microservice.apttrade.api.controller;

import com.microservice.apttrade.api.dto.ItemsResponse;
import com.microservice.apttrade.api.service.AptTradeApiService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/aptTrade")
@RestController
public class AptTradeApiController {

    @Autowired
    private AptTradeApiService aptTradeApiService;


    @ApiOperation(value = "당해 연도/분기 및 주소 코드 별 아파트 실거래 데이터 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rnMgtSn",   value = "도로명 코드",  required = true, dataType = "string", paramType ="path"),
            @ApiImplicitParam(name = "buldMnnm", value = "도로명 건물 본번호 코드", required = true, dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "buldSlno", value = "도로명 건물 부번호 코드", required = true, dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "quarter",   value = "분기",       required = true, dataType = "int", paramType = "query", defaultValue = "1")
    })
    @RequestMapping(value = "/list/{rnMgtSn}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<ItemsResponse> listByAmdCodeAndYear(@PathVariable(name = "rnMgtSn") String rnMgtSn,
                                                    @RequestParam(name = "buldMnnm") long buldMnnm,
                                                    @RequestParam(name = "buldSlno") long buldSlno,
                                                    @RequestParam(defaultValue = "1", required = false) int quarter){

        return aptTradeApiService.listByAddrCodeAndQuarter(rnMgtSn,buldMnnm, buldSlno, quarter);
    }

    @ApiOperation(value = "년도/분기 및 주소 코드 별 아파트 실거래 데이터 목록 조회 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rnMgtSn", value = "도로명 코드", required = true, dataType = "string", paramType ="path"),
            @ApiImplicitParam(name = "buldMnnm", value = "도로명 건물 본번호 코드", required = true, dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "buldSlno", value = "도로명 건물 부번호 코드", required = true, dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "dealYm",  value = "계약 년월",   required = true, dataType = "string",  paramType = "query")
    })
    @RequestMapping(value = "/year/list/{rnMgtSn}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<ItemsResponse> listByAddressAndDealYm(@PathVariable(name = "rnMgtSn") String rnMgtSn,
                                                      @RequestParam(name = "buldMnnm") long buldMnnm,
                                                      @RequestParam(name = "buldSlno") long buldSlno,
                                                      @RequestParam String dealYm){
        return aptTradeApiService.listByAddrCodeAndDealYm(rnMgtSn, buldMnnm, buldSlno, dealYm);

    }

    @ApiOperation(value = "상세 조건(주소, 실거래가, 면적, 년도/월) 별 아파트 실거래 데이터 목록 조회 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rnMgtSn",   value = "도로명 코드",  required = true, dataType = "string", paramType ="path"),
            @ApiImplicitParam(name = "buldMnnm", value = "도로명 건물 본번호 코드", required = true, dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "buldSlno", value = "도로명 건물 부번호 코드", required = true, dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "price",   value = "실거래 금액 범위", required = true, dataType = "string",   paramType = "query"),
            @ApiImplicitParam(name = "area",    value = "전용 면적 범위",  required = true, dataType = "string",  paramType = "query"),
            @ApiImplicitParam(name = "quarter",  value = "분기", required = true, dataType = "int", paramType = "query"),
    })
    @RequestMapping(value = "/condition/list/{rnMgtSn}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<ItemsResponse> listByConditions(@PathVariable(name = "rnMgtSn") String rnMgtSn,
                                                @RequestParam(name = "buldMnnm") long buldMnnm,
                                                @RequestParam(name = "buldSlno") long buldSlno,
                                                @RequestParam(value = "priceRange") String priceRange,
                                                @RequestParam(value = "areaRange")  String areaRange,
                                                @RequestParam(value = "quarter") int quarter ){
        return aptTradeApiService.getByCondition(rnMgtSn, buldMnnm, buldSlno, priceRange, areaRange, quarter);

    }

    @ApiOperation(value = "주소 및 년도 별 아파트 실거래 데이터 목록 조회 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "address",  value = "주소",         required = true, dataType = "string", paramType ="query"),
            @ApiImplicitParam(name = "delYm",    value = "계약 년월",     required = true, dataType = "string",   paramType = "query"),
    })
    @RequestMapping(value = "/address/list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<ItemsResponse>  listByAddress(@RequestParam String address,
                                              @RequestParam String dealYm){

        return aptTradeApiService.listByAddressAndDamYm(address, dealYm);
    }

}
