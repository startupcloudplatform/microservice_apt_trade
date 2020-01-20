package com.microservice.apttrade.api.service;


import com.microservice.apttrade.address.dto.AddressData;
import com.microservice.apttrade.address.dto.AddressDetailData;
import com.microservice.apttrade.address.service.AddressApiService;
import com.microservice.apttrade.api.dto.AptTradeItemsResponse;
import com.microservice.apttrade.api.dto.AptTradeResponse;
import com.microservice.apttrade.api.dto.ItemsResponse;
import com.microservice.apttrade.util.DataException;
import com.microservice.apttrade.util.Paging;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RefreshScope
public class AptTradeApiService {
	private static final Logger logger = LoggerFactory.getLogger(AptTradeApiService.class);

	@Autowired
	private AddressApiService addressApiService;

	@Autowired
	private RequestService requestService;

	/**
	 * 당해 연도 및 주소 코드 별 아파트 실거래 데이터 목록 조회
	 * @param rnMgtSn 도로명
	 * @param buldMnnm 도로명 건물 본번호 코드
	 * @param buldSlno 도로명 건물 부번호 코드
	 * @param quarter 분기
	 */
	public List<ItemsResponse> listByAddrCodeAndQuarter(String rnMgtSn, long buldMnnm, long buldSlno, int quarter){

		List<ItemsResponse> items = new ArrayList<>();
		String lawdCd = rnMgtSn.substring(0,5); //지역코드
		String rnCode = rnMgtSn.substring(5);
		logger.info("rnCode : " + rnCode);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		int end   = quarter * 3;
		int start = end - 3 +1;

		for( int i=start; i<end+1; i++){
			String dealYm = year+"";
			int page = 1;
			if( i < 10 ) dealYm += "0"+i;
			List<AptTradeResponse> list = new ArrayList<>();
			getAptTradeList(list, lawdCd, dealYm, page);
			items= filter(list, items, rnCode, buldMnnm, buldSlno);
		}

		return items;

	}

	/**
	 * 년도/월 및 주소코드 별 아파트 실거래 데이터 목록 조회
	 * @param rnMgtSn 도로명 코드
	 * @param buldMnnm 도로명 건물 본번호 코드
	 * @param buldSlno 도로명 건물 부번호 코드
	 * @param dealYm 계약 년/월
	 */
	public List<ItemsResponse> listByAddrCodeAndDealYm(String rnMgtSn,long buldMnnm, long buldSlno, String dealYm){
		List<ItemsResponse> items = new ArrayList<>();
		String lawdCd = rnMgtSn.substring(0,5); //
		String rnCd   = rnMgtSn.substring(5); //

		int page = 1;

		List<AptTradeResponse> list = new ArrayList<>();
		getAptTradeList(list , lawdCd, dealYm, page );
		items = filter(list, items, rnCd, buldMnnm, buldSlno);

		return items;
	}

	/**
	 * 페이지 별 아파트 실거래가 조회
	 * @param list 조회한 실거래가 데이터
	 * @param lawdCd 지역코드
	 * @param dealYm 계약 년/월
	 * @param page 페이지
	 */
	public List<AptTradeResponse> getAptTradeList(List<AptTradeResponse> list, String lawdCd, String dealYm, int page){
		try{
			AptTradeResponse aptTradeResponse = null;
			String contents = requestService.request(lawdCd, dealYm, page );
			if(!StringUtils.isEmpty(contents) && contents != null){
				aptTradeResponse = requestService.setResponseBody(contents);
			}

			if(aptTradeResponse != null && aptTradeResponse.getItems().size() > 0){
				list.add(aptTradeResponse);
				//Paging paging = new Paging(한 화면에 보여질 글 수 , 페이지 분할 수 , 총 글의 갯수  , 현재 보고 있는 페이지 번호  );)
				int numOfRows  = aptTradeResponse.getBody().getNumOfRows();
				int pageNo     = aptTradeResponse.getBody().getPageNo();
				int totalCount = aptTradeResponse.getBody().getTotalCount();
				int pageSize   = new Paging(numOfRows, totalCount ).getPageCount();
				pageSize = (pageSize > 1) ? pageSize +1 : pageSize;

				Paging paging  = new Paging(numOfRows,pageSize, totalCount, pageNo );
				boolean isNext = paging.isNext();
				if( isNext ){
					getAptTradeList(list, lawdCd, dealYm, pageNo+1 );
				}
			}

		}catch (Exception e){
			logger.warn(e.getMessage());
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 상세 조건(주소, 실거래가, 면적, 년도/월) 별 아파트 실거래 데이터 목록 조회
	 * @param price 가격 범위
	 * @param area 면적 범위(제곱미터 기준)
	 * @param quarter 분기
	 */
	public List<ItemsResponse> getByCondition(String rnMgtSn,long buldMnnm, long buldSlno, String price, String area, int quarter) {

		List<ItemsResponse> items = listByAddrCodeAndQuarter(rnMgtSn, buldMnnm, buldSlno, quarter);
		if(items.size() > 0 ){
			items = filterByPriceAndArea(price, area, items);
		}

		return items;
	}

	/**
	 * 건물본번호 + 건물부번호 + 도로명 코드와 맞는 실거래가 데이터 추출
	 * @param list 조회한 실거래가 데이터
	 * @param rnCode 도로명 코드
	 * @param buldMnnm 도로명 건물 본번호 코드
	 * @param buldSlno 도로명 건물 부번호 코드
	 */
	public List<ItemsResponse> filter(List<AptTradeResponse> list, List<ItemsResponse> filterItems, String rnCode, long buldMnnm, long buldSlno){
		for ( AptTradeResponse rs : list){
			for( AptTradeItemsResponse item : rs.getItems() ){

				if(String.valueOf(item.getRnMgtSn()).equals(rnCode) ){
					if( item.getBuldMnnm() == buldMnnm && item.getBuldSlno() == buldSlno ){
						ItemsResponse ir = new ItemsResponse(item.getRoadName(),  item.getAptName(),  item.getTransactionAmount(),
								item.getYear(),      item.getMonth(),    item.getArea(),  item.getFloor());
						filterItems.add(ir);
					}

				}
			}
		}
		return filterItems;
	}

	/**
	 * 실거래가 범위 및 전용 면적 범위 조건에 맞는 실거래가 데이터 추출
	 * @param priceRange 실거래가가 범위
	 * @param areaRange  전용면적 범위
	 * @param items 조회한 실거래가 데이터
	 */
	public List<ItemsResponse> filterByPriceAndArea(String priceRange, String areaRange, List<ItemsResponse> items ){
		List<ItemsResponse> filterItems = new ArrayList<>();

		for( ItemsResponse item : items ){
			boolean check=true;
			//1. 가격비교
			String[] splitPrice = priceRange.split("-");
			if( splitPrice.length != 0 ){
				int minPrice = Integer.parseInt(priceRange.split("-")[0]);
				int maxPrice = Integer.parseInt(priceRange.split("-")[splitPrice.length-1]);
				String transactionAmount;
				StringBuilder sb = new StringBuilder();
				for( String price: item.getTransactionAmount().split(",") ){
					sb.append(price);
				}
				transactionAmount = StringUtils.isEmpty(sb) ? item.getTransactionAmount() : sb.toString();
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
					if( !(item.getArea() >= minArea) ){
						check =false;
					}
				}else if(!(item.getArea() >= minArea && item.getArea() <= maxArea) ){
					check =false;
				}
			}

			if( check ){
				filterItems.add(item);
			}
		}
		return filterItems;

	}

	/**
	 * 상세 주소및 계약년도 조건을 통해 아파트 실거래가 조회
	 * @param address 상세 주소
	 * @param dealYm  계약년월
	 */
	public List<ItemsResponse> listByAddressAndDamYm(String address, String dealYm){
		List<ItemsResponse> items = new ArrayList<>();
		AddressData addressData = addressApiService.listAddressByKeyword(1, 100, address);
		if(addressData.getJuso() == null){
			throw new DataException.DefaultException(addressData.getCommon().getMessage());
		}else if( addressData.getJuso().size() > 1 ){
			throw new DataException.AddressDataException();
		}else if( addressData.getJuso().size() == 0 ){
			throw new DataException.AddressDataNotFound();
		}

		AddressDetailData juso = addressData.getJuso().get(0);
		long buldMnnm = Long.parseLong(juso.getBuldMnnm());
		long buldSlno = Long.parseLong(juso.getBuldSlno());
		String lawdCd = juso.getRnMgtSn().substring(0,5); //지역코드
		String rnCode = juso.getRnMgtSn().substring(5);


		List<AptTradeResponse> list = new ArrayList<>();
		getAptTradeList(list, lawdCd, dealYm, 1);
		items= filter(list, items, rnCode, buldMnnm, buldSlno);

		return items;
	}


}
