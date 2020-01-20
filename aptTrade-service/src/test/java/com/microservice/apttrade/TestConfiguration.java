package com.microservice.apttrade;

import com.microservice.apttrade.address.dto.AddressData;
import com.microservice.apttrade.address.dto.AddressDetailData;
import com.microservice.apttrade.api.dto.AptTradeBodyResponse;
import com.microservice.apttrade.api.dto.AptTradeItemsResponse;
import com.microservice.apttrade.api.dto.AptTradeResponse;
import com.microservice.apttrade.api.dto.ItemsResponse;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.List;

public class TestConfiguration {
	final static public String RN_MGT_SN = "320913209136";
	final static public String LAWD_CD  = "3209136";
	final static public String DEAL_YM  = "201910";
	final static public String URL      = "http://182.252.131.40:9000/apiservice/4357";
	final static public String AUTH_KEY = "0543fgrib0ggh48ou4rtbgad1ov8820n8";
	final static public String AUTH_TEST_KEY = "1234";
	final static public String ADDRESS  = "경기도 김포시 운양동 5-6";
	final static public String PRICE    = "35000-50000";
	final static public String AREA     = "30-60";
	final static public long   BULD_MNNM = 149;
	final static public long   BULD_SLNO = 0;
	final static public int    QUARTER    = 1;
	final static public int    PAGE       = 1;
	final static public int    PAGE_SIZE  = 10;


	public static AddressData setAddressDataList(){
		AddressData data = new AddressData();
		List<AddressDetailData> detailList = new ArrayList<>();
		AddressDetailData detail1 = new AddressDetailData("",
															"경기도 김포시 금포로 1127 (운양동)",
															"경기도 김포시 운양동 5-6",
															"10092",
															"4157010300",
															"415703209136",
															"4157010300100020005000001",
															"",
															"0",
															"경기도",
															"김포시",
															"운양동",
															"금포로",
															"1127",
															"0",
															"5",
															"6" ,
															"1");

		AddressDetailData detail2 = new AddressDetailData("",
													"경기도 김포시 금포로 1190 (운양동)",
													"경기도 김포시 운양동 7-14",
													"10075",
													"4157010300",
													"415703209136",
													"4157010300100020005000001",
													"",
													"0",
													"경기도",
													"김포시",
													"운양동",
													"금포로",
													"1190",
													"0",
													"7",
													"14",
													"1");

		detailList.add(detail1);
		detailList.add(detail2);
		data.setJuso(detailList);

		return data;
	}

	public static AddressData setAddressData(){
		AddressData data = new AddressData();
		List<AddressDetailData> detailList = new ArrayList<>();
		AddressDetailData detail1 = new AddressDetailData("",
															"경기도 김포시 금포로 1127 (운양동)",
															"경기도 김포시 운양동 5-6",
															"10092",
															"4157010300",
															"415703209136",
															"4157010300100020005000001",
															"",
															"0",
															"경기도",
															"김포시",
															"운양동",
															"금포로",
															"1127",
															"0",
															"5",
															"6",
															"1");

		detailList.add(detail1);
		data.setJuso(detailList);

		return data;
	}

	public static String setAptTradeResponseToString(){
		String contents ="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
		contents += "<response><header><resultCode>00</resultCode><resultMsg>NORMAL SERVICE.</resultMsg></header><body><items><item><거래금액>    18,000</거래금액><건축년도>1990</건축년도><년>2019</년><도로명>북변중로</도로명><도로명건물본번호코드>00104</도로명건물본번호코드><도로명건물부번호코드>00005</도로명건물부번호코드><도로명시군구코드>41570</도로명시군구코드><도로명일련번호코드>01</도로명일련번호코드><도로명지상지하코드>0</도로명지상지하코드><도로명코드>3209056</도로명코드><법정동> 북변동</법정동><법정동본번코드>0184</법정동본번코드><법정동부번코드>0000</법정동부번코드><법정동시군구코드>41570</법정동시군구코드><법정동읍면동코드>10100</법정동읍면동코드><법정동지번코드>1</법정동지번코드><아파트>한일</아파트><월>10</월><일>1</일><일련번호>41570-12</일련번호><전용면적>46.86</전용면적><지번>184</지번><지역코드>41570</지역코드><층>3</층></item><item><거래금액>    24,800</거래금액><건축년도>1998</건축년도><년>2019</년><도로명>사우중로73번길</도로명><도로명건물본번호코드>00011</도로명건물본번호코드><도로명건물부번호코드>00000</도로명건물부번호코드><도로명시군구코드>41570</도로명시군구코드><도로명일련번호코드>01</도로명일련번호코드><도로명지상지하코드>0</도로명지상지하코드><도로명코드>4427276</도로명코드><법정동> 북변동</법정동><법정동본번코드>0815</법정동본번코드><법정동부번코드>0000</법정동부번코드><법정동시군구코드>41570</법정동시군구코드><법정동읍면동코드>10100</법정동읍면동코드><법정동지번코드>1</법정동지번코드><아파트>풍년마을(한라)</아파트><월>10</월><일>2</일><일련번호>41570-11</일련번호><전용면적>59.97</전용면적><지번>815</지번><지역코드>41570</지역코드><층>11</층></item></items>";
		contents += "<numOfRows>2</numOfRows><pageNo>1</pageNo><totalCount>586</totalCount></body></response>";
		return contents;
	}

	public static AptTradeResponse setAptTradeResponse(){
		AptTradeResponse  aptTradeResponse = new AptTradeResponse();
		AptTradeBodyResponse body = new AptTradeBodyResponse(1, 2, 100);

		List<AptTradeItemsResponse> items = new ArrayList<>();
		AptTradeItemsResponse item = new AptTradeItemsResponse();
		item.setAptName("이편한세상");
		item.setArea(100);
		item.setBuldMnnm(1127);
		item.setBuldSlno(0);
		item.setFloor(15);
		item.setMonth(3);
		item.setRnMgtSn(3209136);
		item.setRoadName("경기도 김포시");
		item.setTransactionAmount("55,000");
		item.setYear(201901);
		items.add(item);

		AptTradeItemsResponse item2 = new AptTradeItemsResponse();
		item2.setAptName("이편한세상");
		item2.setArea(100);
		item2.setBuldMnnm(149);
		item2.setBuldSlno(0);
		item2.setFloor(15);
		item2.setMonth(6);
		item2.setRnMgtSn(3209136);
		item2.setRoadName("경기도 김포시");
		item2.setTransactionAmount("55,000");
		item2.setYear(201902);
		items.add(item2);

		aptTradeResponse.setBody(body);
		aptTradeResponse.setItems(items);
		return aptTradeResponse;
	}

	public static List<AptTradeResponse> setListAptTradeResponse(){
		List<AptTradeResponse> list = new ArrayList<>();
		list.add(setAptTradeResponse());

		return list;
	}

	public static List<ItemsResponse> setItems(){
		List<ItemsResponse> items = new ArrayList<>();
		ItemsResponse item = new ItemsResponse();
		item.setAptName("이편한세상");
		item.setArea(100);
		item.setFloor(15);
		item.setMonth(6);
		item.setRoadName("경기도 김포시");
		item.setTransactionAmount("55,000");
		item.setYear(201902);

		items.add(item);

		return items;
	}



}
