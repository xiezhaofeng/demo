package com.jers.account.conterllor;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jers.account.entity.User;
import com.jers.account.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
    private UserService userService;

    /**
     * 目前图简便，就用缺省的配置了
     */
    static {
        BasicConfigurator.configure();
    }
	
	@RequestMapping("/{id}")
    private User findById(@PathVariable Integer id) {
        User user = userService.getUser(id);
        LOGGER.info("获取用户id为 {} 的用户，详细信息为 {}", id, user);
        return user;
    }

    @RequestMapping("/hello/{fallback}")
    @HystrixCommand(fallbackMethod="helloFallbackMethod")/*调用方式失败后调用helloFallbackMethod*/
    public String hello(@PathVariable("fallback") String fallback){
        LOGGER.info("execute method hello, fallback: {}", fallback);
        if("1".equals(fallback)){
            throw new RuntimeException("...");
        }
        return "hello";
    }

    public String helloFallbackMethod(String fallback){
        return "fallback 参数值为:"+fallback;
    }

	
}
