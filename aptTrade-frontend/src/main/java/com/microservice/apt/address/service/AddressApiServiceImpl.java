package com.microservice.apt.address.service;

import com.microservice.apt.address.dto.AddressData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RefreshScope
public class AddressApiServiceImpl {

    @Autowired
    private AddressApiService addressApiService;

    /**
     * 키워드를 통한 주소 목록 조회
     * @return 주소데이터
     */
    public AddressData listAddressByKeyword(int page, int pageSize,  String keyword){
        AddressData result = addressApiService.listAddressByKeyword(page, pageSize, keyword);
        return result;
    }

    /**
     * 행정구역 - 시/도 목록 조회
     */
    public List<String> listSido(){
        return addressApiService.listSido();
    }

    /**
     * 행정구역 - 구/군 목록 조회
     * @param sido 시/도
     */
    public List<String> listGugunBySido(String sido){
        return addressApiService.listGugunBySido(sido);
    }

    /**
     * 행정구역 - 동 목록 조회
     * @param sido 시/도
     * @param gugun 구/군
     */
    public List<String> listDongBySidoAndGugun(String sido, String gugun){
        return addressApiService.listDongBySidoAndGugun(sido, gugun);
    }
}
