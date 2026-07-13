/**
 * 从 URL 里接收 interfaceName、methodName、param
 * 用 interfaceName 从 RpcServer 找到对应的实例
 * 用 methodName 从实例里找到对应的方法
 * 用 param 作为参数
 * 执行 实例.方法(参数)
 * 把结果返回给调用者
 *
 * RPC 就是把 '调用哪个实例的哪个方法，传什么参数' 这三个信息，
 * 通过网络传过去，服务端解析后执行，再把结果传回来。
 */
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
    //Spring MVC（Tomcat）根据URL自动传三个参数
    public String invoke(
        @RequestParam("interfaceName") String interfaceName,
        @RequestParam("methodName") String methodName,
        @RequestParam("param") String param) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //通过getService(interfaceName)找到实现类的实例
        Object service=rpcServer.getService(interfaceName);

        //用反射找到对应的方法
        // service 的 Class 是 UserServiceImpl，它里面有 sayHello方法
        Method method=service.getClass().getMethod(methodName, String.class);

        //调用invoke这个方法
        //第一个参数是"哪个对象"（实例），第二个参数是"传给方法的参数值"
        Object inv=method.invoke(service,param);
        return (String)inv;
    }
}
