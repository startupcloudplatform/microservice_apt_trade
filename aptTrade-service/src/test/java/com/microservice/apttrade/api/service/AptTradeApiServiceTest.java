package com.microservice.apttrade.api.service;

import com.microservice.apttrade.AptTradeApplication;
import com.microservice.apttrade.TestConfiguration;
import com.microservice.apttrade.address.service.AddressApiService;
import com.microservice.apttrade.api.dto.ItemsResponse;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AptTradeApplication.class, properties = {
		"spring.cloud.discovery.enabled=false",
		"spring.cloud.config.discovery.enabled = false",
		"spring.cloud.config.enabled = false"
})
@TestPropertySource(locations = {"classpath:application-test.properties"})
public class AptTradeApiServiceTest {
	private final Logger logger = LoggerFactory.getLogger(AptTradeApiServiceTest.class);

	MockMvc mockMvc;

	@InjectMocks
	private AptTradeApiService aptTradeApiService;

	@Mock
	private RequestService requestService;

	@Mock
	private AddressApiService addressApiService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(aptTradeApiService).build();

		OkHttpClient client = new OkHttpClient.Builder()
				.connectTimeout(10, TimeUnit.MINUTES)
				.writeTimeout(60, TimeUnit.SECONDS)
				.readTimeout(60, TimeUnit.SECONDS)
				.build();

		new RequestService(TestConfiguration.AUTH_KEY, TestConfiguration.URL, client, "10");
		when(requestService.request(anyString(), anyString(), anyInt())).thenReturn(TestConfiguration.setAptTradeResponseToString());
		when(requestService.setResponseBody(TestConfiguration.setAptTradeResponseToString())).thenReturn(TestConfiguration.setAptTradeResponse());

	}

	@Test
	public void listByAmdCodeAndYear() throws Exception {
		String rnMgtSn  = TestConfiguration.RN_MGT_SN;
		int  quarter    = TestConfiguration.QUARTER;
		long buldMnnm  = TestConfiguration.BULD_MNNM;
		long buldSlno  = TestConfiguration.BULD_SLNO;

		List<ItemsResponse> items= aptTradeApiService.listByAddrCodeAndQuarter(rnMgtSn,buldMnnm, buldSlno, quarter);
		assertTrue(items.size() > 0);
		logger.info(items.toString());
		logger.info("====================================> " + items.size());
	}

	@Test
	public void listByAddrCodeAndYearAndMonth() throws Exception {

		String rnMgtSn  = TestConfiguration.RN_MGT_SN;
		String dealYm   = TestConfiguration.DEAL_YM;
		long buldMnnm  = TestConfiguration.BULD_MNNM;
		long buldSlno  = TestConfiguration.BULD_SLNO;


		List<ItemsResponse> items= aptTradeApiService.listByAddrCodeAndDealYm(rnMgtSn,buldMnnm, buldSlno, dealYm);
		assertTrue(items.size() > 0);
		logger.info(items.toString());
		logger.debug(items.toString());
	}

	@Test
	public void listByAddressAndDamYm() throws Exception{
		when(addressApiService.listAddressByKeyword(anyInt(), anyInt(), anyString())).thenReturn(TestConfiguration.setAddressData());
		List<ItemsResponse> items= aptTradeApiService.listByAddressAndDamYm(TestConfiguration.ADDRESS, TestConfiguration.DEAL_YM);
		logger.info(items.toString());
	}
}
