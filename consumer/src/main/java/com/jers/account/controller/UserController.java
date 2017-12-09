package com.jers.account.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.jers.account.consumer.FeignService;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.jers.account.consumer.UserConsumer;
import com.jers.account.entity.User;
import com.netflix.discovery.EurekaClient;

@RestController
@RequestMapping(value = "/", produces = "application/json; charset=utf-8")
public class UserController
{

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private EurekaClient eurekaClient;

	@Autowired
	private UserConsumer userConsumer;

	@Autowired
	private FeignService feignService;

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping("user/{id}")
	public String findById(HttpServletRequest request)
	{
//		User user = userConsumer.findById(id);
//		LOGGER.info("获取到的用户id为 {}, User为 {}", id, user);
		return postRequest(request, null, null);
	}

	private String postRequest(HttpServletRequest request, String requestBody, ContentType contentType)
	{
		String url = "http://MICROSERVICE-PROVIDER-USER"+request.getRequestURI();
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody, headers);
		if(contentType != null){
			headers.set(HttpHeaders.CONTENT_TYPE, contentType.getMimeType());
		}
		ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
		return exchange.getBody();
	}

	@RequestMapping("/discovery")
	public String doDiscoveryService()
	{
		StringBuilder buf = new StringBuilder();
		List<ServiceInstance> serviceInstsces = discoveryClient.getInstances("STORES");
		List<String> serviceIds = discoveryClient.getServices();
		if (!CollectionUtils.isEmpty(serviceIds))
		{
			for (String s : serviceIds)
			{
				System.out.println("serviceId:" + s);
				List<ServiceInstance> serviceInstances = discoveryClient.getInstances(s);
				if (!CollectionUtils.isEmpty(serviceInstances))
				{
					for (ServiceInstance si : serviceInstances)
					{
						buf.append("[" + si.getServiceId() + " host=" + si.getHost() + " port=" + si.getPort() + " uri=" + si.getUri() + "]");
					}
				}
				else
				{
					buf.append("no service.");
				}
			}
		}

		return buf.toString();
	}

	@RequestMapping("test/{fallback}")
	public String test(@PathVariable String fallback){
		String res = feignService.hello(fallback);
		return "invoke result:"+res;
	}
}
