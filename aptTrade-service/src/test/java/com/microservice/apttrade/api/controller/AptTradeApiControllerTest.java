package com.microservice.apttrade.api.controller;

import com.microservice.apttrade.AptTradeApplication;
import com.microservice.apttrade.TestConfiguration;
import com.microservice.apttrade.api.service.AptTradeApiService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AptTradeApplication.class, properties = {
        "spring.cloud.discovery.enabled=false",
        "spring.cloud.config.discovery.enabled = false",
        "spring.cloud.config.enabled = false"
})
@TestPropertySource(locations = {"classpath:application-test.properties"})
public class AptTradeApiControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(AptTradeApiControllerTest.class);

    private MockMvc mockMvc;

    @InjectMocks
    private AptTradeApiController aptTradeApiController;
    @Mock
    private AptTradeApiService aptTradeApiService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(aptTradeApiController).build();
    }

    @Test
    public void listByAmdCodeAndYear() throws Exception{
        String rnMgtSn = TestConfiguration.RN_MGT_SN;
        int  page      = TestConfiguration.PAGE;
        long buldMnnm = TestConfiguration.BULD_MNNM;
        long buldSlno = TestConfiguration.BULD_SLNO;


        when(aptTradeApiService.listByAddrCodeAndQuarter(rnMgtSn,buldMnnm, buldSlno, page)).thenReturn(TestConfiguration.setItems());
        this.mockMvc.perform(get("/api/aptTrade/list/"+rnMgtSn)
                .param("quarter",      String.valueOf(page))
                .param("buldMnnm", String.valueOf(buldMnnm))
                .param("buldSlno", String.valueOf(buldSlno))
                .contentType( MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$").isArray())
                .andReturn();
    }

    @Test
    public void listByConditions() throws Exception{
        String rnMgtSn = TestConfiguration.RN_MGT_SN;
        String priceRange = TestConfiguration.PRICE;
        String areaRange  = TestConfiguration.AREA;
        long buldMnnm = TestConfiguration.BULD_MNNM;
        long buldSlno = TestConfiguration.BULD_SLNO;
        int  page     = TestConfiguration.PAGE;
        int quarter   = TestConfiguration.QUARTER;

        when(aptTradeApiService.getByCondition(rnMgtSn,buldMnnm, buldSlno,priceRange, areaRange, quarter )).thenReturn(TestConfiguration.setItems());
        this.mockMvc.perform(get("/api/aptTrade/condition/list/"+rnMgtSn)
                .param("quarter",      String.valueOf(page))
                .param("buldMnnm", String.valueOf(buldMnnm))
                .param("buldSlno", String.valueOf(buldSlno))
                .param("priceRange", priceRange)
                .param("areaRange", areaRange)
                .param("quarter", String.valueOf(quarter))
                .contentType( MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$").isArray())
                .andReturn();
    }


    @Test
    public void listByAddress() throws Exception{
        String address = TestConfiguration.ADDRESS;
        String dealYm  = TestConfiguration.DEAL_YM;

        when(aptTradeApiService.listByAddressAndDamYm(address, dealYm )).thenReturn(TestConfiguration.setItems());
        this.mockMvc.perform(get("/api/aptTrade/address/list")
                .param("address", address)
                .param("dealYm", dealYm)
                .contentType( MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$").isArray())
                .andReturn();
    }

}
