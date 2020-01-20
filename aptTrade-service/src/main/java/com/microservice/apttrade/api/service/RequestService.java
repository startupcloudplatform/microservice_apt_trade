package com.microservice.apttrade.api.service;

import DataException.AptTransactionDataException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.apttrade.api.dto.AptTradeBodyResponse;
import com.microservice.apttrade.api.dto.AptTradeItemsResponse;
import com.microservice.apttrade.api.dto.AptTradeResponse;
import com.microservice.apttrade.util.DataException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RequestService {
	private static final Logger logger = LoggerFactory.getLogger(RequestService.class);
	private static String PAGE_SIZE = "1000";

	@Value("${opendata.api.key:}")
	String authKey;

	@Value("${opendata.api.apt.trade.uri:}")
	String uri;

	OkHttpClient client= null;

	public RequestService(){}
	public RequestService(String authKey, String uri, OkHttpClient client, String pageSize){
		this.authKey = authKey;
		this.uri = uri;
		this.client = client;
		this.PAGE_SIZE = pageSize;
	}


	/**
	 * 아파트 실거래가 조회 API
	 * @param lawdCd 지역코드
	 * @param dealYm 계약 년/월
	 * @param page 페이지
	 */
	public String request(String lawdCd, String dealYm, int page) {
		String contents ="";
		if( client == null ){
			client = new OkHttpClient();
			client = new OkHttpClient.Builder()
					.connectTimeout(10, TimeUnit.MINUTES)
					.writeTimeout(60, TimeUnit.SECONDS)
					.readTimeout(60, TimeUnit.SECONDS)
					.build();

		}

		try{
			HttpUrl httpUrl = HttpUrl.parse(uri).newBuilder()
					.addQueryParameter("auth_key",  authKey)
					.addQueryParameter("LAWD_CD",   lawdCd)
					.addQueryParameter("DEAL_YMD",  dealYm)
					.addQueryParameter("pageNo",    String.valueOf(page))
					.addQueryParameter("numOfRows", PAGE_SIZE).build();

			//실거래가 조회
			String url = httpUrl.toString();
			Request request = new Request.Builder().url(url)
												.header("Content-Type", "application/json")
												.addHeader("ACCEPT", "application/json")
												.build();
			Response response = client.newCall(request).execute();
			if( response.code() == HttpStatus.OK.value() && response.body() != null){
				contents  = response.body().string();
			}

		}catch (Exception e){
			e.printStackTrace();
		}

		logger.info("contentx========================="+contents);
		return contents;
	}

	public AptTradeResponse setResponseBody(String body) {
		AptTradeResponse aptTradeResponse =  new AptTradeResponse();
		if( body != null ){
			if( body.contains("일일 요청 횟수 초과") ){
				throw new DataException.AptTransactionDataException();
			}
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			JSONObject xmlJSONObj = XML.toJSONObject(body);
			if( xmlJSONObj.toString().equals("{}") ){
				return aptTradeResponse;
			}

			String jsonToString = xmlJSONObj.toString();

			try{
				JsonNode jsonNodeRoot = mapper.readTree(jsonToString);
				JsonNode headerNode = jsonNodeRoot.get("response").get("header").get("resultCode");
				if(headerNode.asText().equals("00")){
					JsonNode bodyNode = jsonNodeRoot.get("response").get("body");
					JsonNode itemNode = jsonNodeRoot.get("response").get("body").get("items").get("item");

					if(itemNode != null){
						aptTradeResponse = new AptTradeResponse();
						AptTradeBodyResponse bodyResponse =  mapper.readValue(bodyNode.toString(), new TypeReference<AptTradeBodyResponse>() {});
						List<AptTradeItemsResponse> items =  mapper.readValue(itemNode.toString(), new TypeReference<ArrayList<AptTradeItemsResponse>>() {});
						aptTradeResponse.setBody(bodyResponse);
						aptTradeResponse.setItems(items);
					}
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}else{
			throw new DataException.AptTransactionDataException();
		}

		return aptTradeResponse;
	}


}
