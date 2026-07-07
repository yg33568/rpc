/**
 * 服务注册表(服务注册与发现机制)
 * 注册服务：把服务名字和实现类实例存进电话簿（Map）。
 * 提供查询：别人给一个服务名字，能从电话簿里找到对应的实例。
 */

package com.yg.provider;

import com.yg.api.UserService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class RpcServer {


    private Map<String, Object> serviceMap;
    public RpcServer(){
        serviceMap=new HashMap<>();
        register("com.yg.api.UserService",new UserServiceImpl());
    }
    public void register(String interfaceName, Object serviceImpl){
        serviceMap.put(interfaceName,  serviceImpl);
        System.out.println("注册服务：" + interfaceName);
    }
    public Object getService(String interfaceName){
        return serviceMap.get(interfaceName);

    }

//    public static void main(String[] args) {
//        RpcServer server=new RpcServer();
//        UserService service=(UserService)server.getService("com.yg.api.UserService");
//        service.sayHello("小王");
//    }

}


