package com.yg.consumer;

import com.yg.api.UserService;
import com.yg.provider.UserServiceImpl;

public class ClientTest {
    public static void main(String[] args) {
        UserService userService=RpcProxyFactory.getProxy(UserService.class);
        String result=userService.sayHello("小王");
        System.out.println(result);
    }
}
