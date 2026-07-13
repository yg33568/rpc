/**
 * 服务注册表
 * 注册服务：把服务名字和实现类实例存进电话簿（Map）。
 * 提供查询：别人给一个服务名字，能从电话簿里找到对应的实例。
 * 其实就是把全类名和实现类实例存进map中，方便后续的存取
 */

package com.yg.provider;

import com.yg.api.UserService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class RpcServer {

    //创建一个集合存储所有的service，方便后续的代理
    private Map<String, Object> serviceMap;

    public RpcServer(){
        serviceMap=new HashMap<>();
        // TODO: 后续改成自动扫描注册，不用手动写死
        register("com.yg.api.UserService",new UserServiceImpl());
    }

    //将全类名和实现类的实例绑定在一起存入map中
    public void register(String interfaceName, Object serviceImpl){
        serviceMap.put(interfaceName,  serviceImpl);
        System.out.println("注册服务：" + interfaceName);
    }

    //将通过 interfaceName全类名获取到该全类名下的实例
    public Object getService(String interfaceName){
        return serviceMap.get(interfaceName);
    }
}


