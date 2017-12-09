package com.jers.account.consumer;/**
 * Created by admin on 2017/12/4.
 */

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author XZF
 * @Title: FeignService
 * @Package com.jers.account.consumer
 * @Description
 * @date 2017/12/4 11:43
 * @Copyrigth 版权所有 (C) 2017 广州讯心信息科技有限公司.
 */
@FeignClient("eureka-provider")
public interface FeignService {

    @RequestMapping("/user/hello/{fallback}")
    String hello(@PathVariable("fallback") String fallback);
}
