package com.microservice.apt.olv.service;

import com.microservice.apt.address.service.AddressFeignConfiguration;
import com.microservice.apt.olv.dto.OlvResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(url = "${feign.olv-api.url}", name="plv-front", configuration = OlvFeignConfiguration.class)
public interface OfficialLandValueService {

	@GetMapping("/api/pblntf/value/building/unit")
	List<OlvResponse> listOfficialLandValuesByPnu(@RequestParam(value = "pnu") String pnu);

}
