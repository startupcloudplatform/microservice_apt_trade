package com.microservice.apttrade.address.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressDetailData {

    @JsonProperty("detBdNmList")
    private String detBdNmList; //상세 건물명

    @JsonProperty("roadAddr")
    private String roadAddr; //도로명 주소

    @JsonProperty("jibunAddr")
    private String jibunAddr; //지번 주소

    @JsonProperty("zipNo")
    private String zipNo; //우편번호

    @JsonProperty("admCd")
    private String admCd; //행정구역코드

    @JsonProperty("rnMgtSn")
    private String rnMgtSn; //도로명 코드

    @JsonProperty("bdMgtSn")
    private String bdMgtSn; //건물관리번호

    @JsonProperty("bdNm")
    private String bdNm; //건물명

    @JsonProperty("bdKdcd")
    private String bdKdcd;//공동주택여부(1 : 공동주택, 0 : 비공동주택)

    @JsonProperty("siNm")
    private String siNm;//시도명

    @JsonProperty("sggNm")
    private String sggNm; //시군구명

    @JsonProperty("emdNm")
    private String emdNm; //읍면동명

    @JsonProperty("rn")
    private String rn;//도로명

    @JsonProperty("buldMnnm")
    private String buldMnnm;//건물본번

    @JsonProperty("buldSlno")
    private String buldSlno;//건물부번

    @JsonProperty("lnbrMnnm")
    private String lnbrMnnm;//지번본번(번지)

    @JsonProperty("lnbrSlno")
    private String lnbrSlno;//지번부번(호)

    @JsonProperty("mtYn")
    private String mtYn;//산여부(0 : 대지, 1 : 산)

}
