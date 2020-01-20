package com.microservice.apt.trade.service;

import com.microservice.apt.trade.dto.ApartmentTradeResponse;
import com.microservice.apt.trade.dto.AptTradeConditionResponse;
import com.microservice.apt.util.DataException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

public class RequestService extends ApiServiceFallBack{

    private static final Logger logger = LoggerFactory.getLogger(RequestService.class);

    @Autowired
    @LoadBalanced
    private RestTemplate restTemplate;

    @Value("${gateway.basic.user:}")
    String user;

    @Value("${gateway.basic.password:}")
    String password;

    private HttpHeaders getHeaders(){
        String basicAuth = String.format("%s:%s", user, password);
        String base64Auth = Base64Utils.encodeToString(basicAuth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.format("Basic %s", base64Auth));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private URI setURI(String uri, MultiValueMap<String, String> maps) {
        return UriComponentsBuilder.fromHttpUrl(uri)
                .queryParams(maps)
                .build()
                .encode()
                .toUri();
    }


    /**
     * 아파트 실거래가 조회 back-service 요청
     * @param rnMgtSn  도로명 코드
     * @param buldMnnm 건물 본번호
     * @param buldSlno 건물 부번호
     * @param quarter  분기
     */
    @HystrixCommand(commandKey = "exception2", fallbackMethod = "requestFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000"),
            @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "10"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000")
    })
    protected ResponseEntity<List<ApartmentTradeResponse>> requestAptTrade(String rnMgtSn, long buldMnnm, long buldSlno, int quarter){

        MultiValueMap<String, String> maps = new LinkedMultiValueMap<>();
        maps.add("rnMgtSn", rnMgtSn);
        maps.add("buldMnnm", String.valueOf(buldMnnm));
        maps.add("buldSlno", String.valueOf(buldSlno));
        maps.add("quarter", String.valueOf(quarter));

        try {
            URI uri = setURI("http://apigateway/trade-service/api/aptTrade/list/"+rnMgtSn, maps);
            HttpEntity<?> request = new HttpEntity<>(getHeaders());
            return restTemplate.exchange(uri, HttpMethod.GET, request, new ParameterizedTypeReference<List<ApartmentTradeResponse>>() {});

        } catch(HttpStatusCodeException e) {
            logger.warn(e.getResponseBodyAsString());
            if(e.getResponseBodyAsString().contains("일일 요청 횟수 초과 ")) {
                throw new DataException();
            }else{
                throw new DataException.DefaultException();
            }
        }
    }


    /**
     * 실거래가 범위 및 전용면적 조건에 맞는 아파트 실거래가 조회 back-service 요청
     * @param priceRange 실거래 가격 범위
     * @param areaRange  전용면적 범위
     * @param quarter    분기
     */
    @HystrixCommand(commandKey = "exception2", fallbackMethod = "requestConditionFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "20000"),
            @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "20000"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "20000")
    })
    protected ResponseEntity<List<AptTradeConditionResponse>> requestAptTradeByCondition(String rnMgtSn, long buldMnnm, long buldSlno, String priceRange, String areaRange, int quarter){

        MultiValueMap<String, String> maps = new LinkedMultiValueMap<>();
        maps.add("buldMnnm", String.valueOf(buldMnnm));
        maps.add("buldSlno", String.valueOf(buldSlno));
        maps.add("priceRange", priceRange);
        maps.add("areaRange",areaRange);
        maps.add("quarter", String.valueOf(quarter));

        try {
            URI uri = setURI("http://apigateway/trade-service/api/aptTrade/condition/list/"+rnMgtSn, maps);
            HttpEntity<?> request = new HttpEntity<>(getHeaders());
            return restTemplate.exchange(uri, HttpMethod.GET, request, new ParameterizedTypeReference<List<AptTradeConditionResponse>>() {});

        } catch(HttpStatusCodeException e) {
            logger.warn(e.getResponseBodyAsString());
            if(e.getResponseBodyAsString().contains("일일 요청 횟수 초과 ")) {
                throw new DataException();
            }else{
                throw new DataException.DefaultException();
            }
        }
    }
}
