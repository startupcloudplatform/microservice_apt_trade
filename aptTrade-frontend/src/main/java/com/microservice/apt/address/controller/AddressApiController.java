package com.microservice.apt.address.controller;

import com.microservice.apt.address.dto.AddressData;
import com.microservice.apt.address.service.AddressApiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressApiController {

    @Autowired
    private AddressApiServiceImpl addressApiService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public AddressData listByKeyword(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize,
                                     @RequestParam String keyword) {
        return addressApiService.listAddressByKeyword(page, pageSize, keyword);

    }

    @RequestMapping(value = "/sido/list", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public List<String> listSido(){
        return addressApiService.listSido();

    }

    @RequestMapping(value = "/gugun/list", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public List<String> listGugunBySido(@RequestParam(value = "sido") String sido){
        return addressApiService.listGugunBySido(sido);
    }

    @RequestMapping(value = "/dong/list", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public List<String> listGugunBySido(@RequestParam(value = "sido")  String sido,
                                        @RequestParam(value = "gugun") String gugun){
        return addressApiService.listDongBySidoAndGugun(sido, gugun);
    }

}
