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

import com.yg.common.RpcRequest;
import com.yg.common.RpcResponse;
import com.yg.provider.RpcServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;

@RestController
public class RpcController {
    @Autowired
    private RpcServer rpcServer;

    @PostMapping("/rpc")
    public RpcResponse invoke(@RequestBody RpcRequest request) {
        try {
            Object service = rpcServer.getService(request.getInterfaceName());
            if (service == null) {
                return RpcResponse.error("找不到服务：" + request.getInterfaceName());
            }
            String[] paramTypeNames = request.getParamTypeNames();
            Class<?>[] paramTypes = new Class<?>[paramTypeNames.length];
            for (int i = 0; i < paramTypeNames.length; i++) {
                paramTypes[i] = getClassByName(paramTypeNames[i]);
            }
            //用反射找到对应的方法
            // service 的 Class 是 UserServiceImpl，它里面有 sayHello方法
            Method method = service.getClass().getMethod(request.getMethodName(), paramTypes);
            //调用invoke这个方法
            //第一个参数是"哪个对象"（实例），第二个参数是"传给方法的参数值"
            Object result = method.invoke(service, request.getParams());
            // 成功：返回 data
            return RpcResponse.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            // 失败：返回错误信息
            return RpcResponse.error(e.getMessage());
        }
    }

    private Class<?> getClassByName(String className) throws ClassNotFoundException {
        return switch (className) {
            case "int" -> int.class;
            case "boolean" -> boolean.class;
            case "byte" -> byte.class;
            case "short" -> short.class;
            case "long" -> long.class;
            case "float" -> float.class;
            case "double" -> double.class;
            case "char" -> char.class;
            default -> Class.forName(className);
        };
    }
}
