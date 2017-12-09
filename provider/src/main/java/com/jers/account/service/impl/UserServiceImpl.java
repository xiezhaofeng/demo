package com.jers.account.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jers.account.entity.User;
import com.jers.account.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public User getUser(Integer id) {
		return users.get(id);
	}

	 private static Map<Integer, User> users = new HashMap<Integer, User>();

	    static {
	        users.put(1, new User(1, "xiaxuan", 24));
	        users.put(2, new User(2, "bingwen", 24));
	    }
}
