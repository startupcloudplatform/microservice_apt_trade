package com.microservice.apt.trade.service;

import com.microservice.apt.address.dto.AddressData;
import com.microservice.apt.address.dto.AddressDetailData;
import com.microservice.apt.address.service.AddressApiService;
import com.microservice.apt.olv.dto.OlvResponse;
import com.microservice.apt.olv.service.OfficialLandValueService;
import com.microservice.apt.trade.dto.ApartmentTradeRatioBody;
import com.microservice.apt.trade.dto.ApartmentTradeRatioHeader;
import com.microservice.apt.trade.dto.ApartmentTradeResponse;
import com.microservice.apt.trade.dto.AptTradeConditionResponse;
import com.microservice.apt.util.DataException;
import com.microservice.apt.util.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RefreshScope
public class AptTradeApiService extends RequestService {

    private static final Logger logger = LoggerFactory.getLogger(AptTradeApiService.class);
    private static final Integer MAX_PAGE_SIZE = 100;

    @Autowired
    private AddressApiService addressApiService;

    @Autowired
    private OfficialLandValueService officialLandValueService;


    /**
     * 공동주택 실거래가 대비 공시지가 비율 조회 by 도로명코드
     * @param rnMgtSn 도로명 코드
     * @param buldMnnm 부번호
     * @param buldSlno 본번호
     * @param quarter  분기
     */
    public List<ApartmentTradeRatioHeader> listByAddrCodeAndYear(String rnMgtSn, String pnu, long buldMnnm, long buldSlno, int quarter){
        List<ApartmentTradeRatioHeader> results = new ArrayList<>();
        ResponseEntity<List<ApartmentTradeResponse>> response = requestAptTrade(rnMgtSn, buldMnnm, buldSlno, quarter);
        if(response != null && response.getBody() != null && response.getBody().size() > 0){
            results  = setApartmentTradeRatio(new ArrayList<>(), response, pnu);
        }
        return results;
    }


    /**
     * 공동주택 실거래가 대비 공시지가 비율 조회 by 행정구역
     * @param address    주소(행정구역)
     * @param priceRange 실거래가 범위
     * @param areaRange  전용 면적 범위
     * @param quarter    거래계약 분기
     */
    public List<ApartmentTradeRatioHeader> listByAddressByCondition(String address,  String priceRange, String areaRange, int quarter){
        List<ApartmentTradeRatioHeader> results = new ArrayList<>();


        String keyword = address.replace("/", " ") ;
        //1. 주소 조회
        List<AddressDetailData> addressList = listAllAddressByKeyword(new ArrayList<>(), keyword, 1);
        if( addressList.size() == 0 ) {
            throw new DataException.AddressDataException();
        }

        //2. 주소 Loop
        for( AddressDetailData juso : addressList ){
            String pnu = juso.getAdmCd() + (Integer.parseInt(juso.getMtYn())+1) + setFormatForPnu(juso.getLnbrMnnm()) + setFormatForPnu(juso.getLnbrSlno());
            //3. 공시지가 조회
            List<OlvResponse> publicPriceResponseList = officialLandValueService.listOfficialLandValuesByPnu(pnu);
            if(publicPriceResponseList.size() > 0 && !publicPriceResponseList.get(0).getDong().equals("-1") ){
                //4. 실거래가 조회
                int buldMnnm = Integer.parseInt(juso.getBuldMnnm());
                int buldSlno = Integer.parseInt(juso.getBuldSlno());
                ResponseEntity<List<AptTradeConditionResponse>> response = requestAptTradeByCondition(juso.getRnMgtSn(),buldMnnm, buldSlno, priceRange, areaRange, quarter);
                ApartmentTradeRatioHeader header = new ApartmentTradeRatioHeader(juso.getRoadAddr(), juso.getBdNm());
                if(response != null && response.getBody() != null && response.getBody().size() > 0){
                    List<ApartmentTradeRatioBody> bodyList = new ArrayList<>();
                    for( AptTradeConditionResponse trade : response.getBody() ){

                        ApartmentTradeResponse aptTradeResponse = new ApartmentTradeResponse(trade.getRoadName(), trade.getAptName(), trade.getTransactionAmount(), trade.getYear(), trade.getMonth(), trade.getArea(), trade.getFloor());
                        ApartmentTradeRatioBody body = new ApartmentTradeRatioBody(aptTradeResponse, publicPriceResponseList);

                        body.setAvgDeclaredValue();
                        body.setRatio();
                        bodyList.add(body);

                        bodyList.sort((arg0, arg1) -> {
                            // TODO Auto-generated method stub
                            double age0 = arg0.getAptTrade().getArea();
                            double age1 = arg1.getAptTrade().getArea();
                            return Double.compare(age0, age1);
                        });
                    }
                    header.setBody(bodyList);
                    results.add(header);
                }
            }
        }

        return results;
    }

    /**
     * 키워드로 조회한 주소 페이징 데이터를 전체 조회
     * @param list    주소 데이터
     * @param keyword 주소 키워드
     * @param page    페이지
     */
    private List<AddressDetailData> listAllAddressByKeyword(List<AddressDetailData> list, String keyword, int page){
        try{
            AddressData response = addressApiService.listAddressByKeyword(page, MAX_PAGE_SIZE, keyword);

            if(response != null && response.getJuso().size() > 0){
                for( AddressDetailData juso : response.getJuso() ){
                    if( juso.getBdKdcd().equals("1") && !StringUtils.isEmpty(juso.getBdNm()) && !StringUtils.isEmpty(juso.getDetBdNmList()) ){
                        logger.info(juso.getJibunAddr());
                        list.add(juso);
                    }
                }
                //Paging paging = new Paging(한 화면에 보여질 글 수 , 페이지 분할 수 , 총 글의 갯수  , 현재 보고 있는 페이지 번호  )
                int numOfRows  = Integer.parseInt(response.getCommon().getCountPerPage());
                int totalCount = Integer.parseInt(response.getCommon().getTotalCount());

                Paging paging  = new Paging(numOfRows,1, totalCount, page );
                boolean isNext = paging.isNext();
                if( isNext ){
                    listAllAddressByKeyword(list, keyword, page+1 );
                }
            }

        }catch (Exception e){
            logger.warn(e.getMessage());
            e.printStackTrace();
        }

        return list;
    }


    /**
     * 조건 검색 필터링
     * @param response   공동주택 실거래가 데이터
     * @param priceRange 실거래 가격 범위
     * @param areaRange  전용면적 범위
     */
    protected ResponseEntity<List<ApartmentTradeResponse>> filter(ResponseEntity<List<ApartmentTradeResponse>> response,String priceRange, String areaRange ){

        List<ApartmentTradeResponse> results = new ArrayList<>();
        ResponseEntity<List<ApartmentTradeResponse>> entity =null;
        if(response.getBody() != null ){
            for( ApartmentTradeResponse data: response.getBody()){
                boolean check=true;
                //1. 가격비교
                String[] splitPrice = priceRange.split("-");
                if( splitPrice.length != 0 ){
                    int minPrice = Integer.parseInt(priceRange.split("-")[0]);
                    int maxPrice = Integer.parseInt(priceRange.split("-")[splitPrice.length-1]);
                    String transactionAmount;
                    StringBuilder sb = new StringBuilder();
                    for( String price: data.getTransactionAmount().split(",") ){
                        sb.append(price);
                    }
                    transactionAmount = StringUtils.isEmpty(sb) ? data.getTransactionAmount() : sb.toString();
                    if( priceRange.split("-").length == 1){
                        if( !(Long.parseLong(transactionAmount) >= minPrice) ){
                            check =false;
                        }
                    }else if( !(Long.parseLong(transactionAmount) >= minPrice &&  Long.parseLong(transactionAmount)  <= maxPrice) ){
                        check =false;
                    }
                }

                //2. 전용면적 비교
                String[] splitArea = areaRange.split("-");
                if (splitArea.length != 0) {
                    double minArea = Double.parseDouble(areaRange.split("-")[0]);
                    double maxArea = Double.parseDouble(areaRange.split("-")[splitArea.length-1]);
                    if( areaRange.split("-").length == 1){
                        if( !(data.getArea() >= minArea) ){
                            check =false;
                        }
                    }else if(!(data.getArea() >= minArea && data.getArea() <= maxArea) ){
                        check =false;
                    }
                }
                if( check ){
                    results.add(data);
                }
            }
        }

        entity =  new ResponseEntity<List<ApartmentTradeResponse>>(results ,HttpStatus.OK);
        return entity;
    }

    /**
     * 공시지가 조회를 위한 PNU 자릿수 Format
     * @param code -> pnu 코드
     */
    private String setFormatForPnu(String code){
        StringBuilder format= new StringBuilder();
        if(code.length() != 4){ //본번,부번 코드가 4자릿 수
            for(int i=0; i<4-code.length(); i++){
                format.append("0");
            }
            format.append(code);
        }else{
            format.append(code);
        }
        return format.toString();
    }

    /**
     * 공동주택 실거래가 대비 공시지가 비율 계산
     * @param results  실거래가 대비 공시지가 비율 결과값
     * @param response 공동주택 실거래가 데이터
     * @param pnu      공시지가 조회 파라미터(pnu)
     */
    private List<ApartmentTradeRatioHeader> setApartmentTradeRatio(List<ApartmentTradeRatioHeader> results, ResponseEntity<List<ApartmentTradeResponse>> response, String pnu){
        //공시지가 조회
        List<OlvResponse> publicPriceResponseList = officialLandValueService.listOfficialLandValuesByPnu(pnu);
        ApartmentTradeRatioHeader header = null;

        if(publicPriceResponseList.size() > 0 && response.getBody().size() > 0){
            //실거래가 Loop
            List<ApartmentTradeRatioBody> bodyList = new ArrayList<>();
            for( ApartmentTradeResponse trade : response.getBody() ){
                header = new ApartmentTradeRatioHeader(trade.getRoadName(), trade.getAptName());

                ApartmentTradeRatioBody body = new ApartmentTradeRatioBody(trade, publicPriceResponseList);
                body.setAvgDeclaredValue();
                body.setRatio();
                bodyList.add(body);

            }

            bodyList.sort((arg0, arg1) -> {
                // TODO Auto-generated method stub
                double age0 = arg0.getAptTrade().getArea();
                double age1 = arg1.getAptTrade().getArea();
                return Double.compare(age0, age1);
            });

            if(header != null){
                header.setBody(bodyList);
            }

            results.add(header);

        }
        return results;
    }

}
