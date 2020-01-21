package com.microservice.apt.address.service;

import com.microservice.apt.address.dto.AddressDTO;
import com.microservice.apt.address.dto.AddressData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(url = "${feign.post-api.url}", name="address-front", configuration = AddressFeignConfiguration.class)
public interface AddressApiService {

    @GetMapping("/v1/address/list")
    AddressData listAddressByKeyword(@RequestParam(value = "page") Integer page,
                                     @RequestParam(value = "pageSize") Integer pageSize,
                                     @RequestParam(value= "keyword") String keyword);

    @GetMapping("/v1/address/sido/list")
    List<String> listSido();

    @GetMapping("/v1/address/gugun/list")
    List<String> listGugunBySido(@RequestParam(value= "sido") String sido);

    @GetMapping("/v1/address/dong/list")
    List<String> listDongBySidoAndGugun(@RequestParam(value= "sido")  String sido,
                                        @RequestParam(value= "gugun") String gugun);

    @GetMapping("/v1/address")
    AddressDTO getAddress(@RequestParam(value= "sido")  String sido,
                          @RequestParam(value= "gugun") String gugun,
                          @RequestParam(value= "dong")  String dong);
}

