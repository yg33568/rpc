/**
 * 生成代理对象
 * 接收一个接口的 Class 对象（比如 UserService.class）
 * 用 JDK 的动态代理生成一个"代理实例"返回
 */
package com.yg.consumer;

import com.yg.api.UserService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class RpcProxyFactory {

    public static <T> T getProxy(Class<T> interfaceClass){

        InvocationHandler handler = (proxy, method, args) -> {
            try {
                // 获取方法名
                String methodName = method.getName();
                // 获取第一个参数（假设只有一个参数）
                String param = java.net.URLEncoder.encode((String) args[0], "UTF-8");
                // 拼 URL
                String url = "http://localhost:8080/rpc?interfaceName="
                        + interfaceClass.getName()
                        + "&methodName=" + methodName
                        + "&param=" + param;
                // 发 HTTP 请求（这里你要用 HttpURLConnection 发 GET 请求）
                // 假设你写了一个 sendGet 方法
                return sendGet(url);
            } catch (Exception e) {
                throw new RuntimeException("远程调用失败", e);
            }
        };

        // 创建代理对象
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),  // 固定写法
                new Class[]{interfaceClass},      // 告诉 Java 这个代理能当 UserService 用
                handler                           // 工作手册
        );
    }

    // 辅助方法：发 HTTP GET 请求，返回响应字符串
    private static String sendGet(String url) throws Exception {
        java.net.URL obj = new java.net.URL(url);
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) obj.openConnection();
        conn.setRequestMethod("GET");

        // 读取响应
        java.io.BufferedReader in = new java.io.BufferedReader(
                new java.io.InputStreamReader(conn.getInputStream(), "UTF-8")
        );
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

}