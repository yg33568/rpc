package com.yg.controller;

import com.yg.provider.RpcServer;
import org.aopalliance.intercept.Invocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RestController
public class RpcController {
    @Autowired
    private RpcServer rpcServer;

    @GetMapping("/rpc")
    public String invoke(
        @RequestParam("interfaceName") String interfaceName,
        @RequestParam("methodName") String methodName,
        @RequestParam("param") String param) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object service=rpcServer.getService(interfaceName);
        //用反射，根据方法名找到对应的方法
        // service 的 Class 是 UserServiceImpl，它里面有 sayHello方法
        Method method=service.getClass().getMethod(methodName, String.class);
        //调用这个方法
        //第一个参数是"哪个对象"，第二个参数是"传给方法的参数值"
        Object inv=method.invoke(service,param);
        return (String)inv;
    }
}
