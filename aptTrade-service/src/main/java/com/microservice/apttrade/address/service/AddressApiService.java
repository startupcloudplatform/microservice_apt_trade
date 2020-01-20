package com.microservice.apttrade.address.service;

import com.microservice.apttrade.address.dto.AddressData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "${feign.post-api.url}", name="address-front", configuration = FeignConfiguration.class)
@Service
public interface AddressApiService {

    @GetMapping("/v1/address/list")
    AddressData listAddressByKeyword(@RequestParam(value = "page") Integer page,
                                     @RequestParam(value = "pageSize") Integer pageSize,
                                     @RequestParam(value = "keyword") String keyword);
}

