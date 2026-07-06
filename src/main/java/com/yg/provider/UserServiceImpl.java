package com.yg.provider;

import com.yg.api.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public String sayHello(String name) {
        System.out.println("服务端收到调用，参数：" + name);
        return "hello"+name;
    }
}
