package com.microservice.apt.trade.service;

import com.microservice.apt.trade.dto.ApartmentTradeRatioHeader;
import com.microservice.apt.trade.dto.ApartmentTradeResponse;
import com.microservice.apt.trade.dto.AptTradeConditionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;

public class ApiServiceFallBack {
    private static final Logger logger = LoggerFactory.getLogger(ApiServiceFallBack.class);

    public List<ApartmentTradeRatioHeader>  defaultFallback() {
        logger.warn("================================== >Invoking fallback for listByAddrCodeAndYear");
        return new ArrayList<>();
    }

    public List<ApartmentTradeRatioHeader> listByAddressAndQuarterFallback(){
        logger.warn("================================== >Invoking fallback for listByAddrCodeAndYear");
        return new ArrayList<>();
    }

    public  ResponseEntity<List<ApartmentTradeResponse>> requestFallback(String rnMgtSn, long buldMnnm, long buldSlno, int quarter, Throwable e){
        logger.info("================================== >Invoking fallback for" + e.getMessage());
        return new ResponseEntity<List<ApartmentTradeResponse>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public  ResponseEntity<List<AptTradeConditionResponse>> requestConditionFallback(String address, String priceRange, String areaRange, int quarter){
        logger.info("================================== >Invoking fallback for");
        return new ResponseEntity<List<AptTradeConditionResponse>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
