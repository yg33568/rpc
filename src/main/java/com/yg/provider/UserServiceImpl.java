package com.yg.provider;

import com.yg.api.UserService;
//实现UserService接口
public class UserServiceImpl implements UserService {

    //重写sayHello方法
    @Override
    public String sayHello(String name) {
        System.out.println("服务端收到sayHello调用，参数：" + name);
        return "hello"+name;
    }

    @Override
    public String getUserInfo(String name, int age, String city) {
        System.out.println("服务端收到getUserInfo调用，参数："+city);
        return "我是"+name+",我今年"+age+"岁了，我住在"+city;
    }
}
