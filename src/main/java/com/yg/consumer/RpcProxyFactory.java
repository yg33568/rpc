package com.yg.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yg.common.RpcRequest;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Proxy;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class RpcProxyFactory {

    private static final String RPC_URL = "http://127.0.0.1:8080/rpc";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                (proxy, method, args) -> {
                    // 1. 封装请求
                    RpcRequest request = new RpcRequest();
                    request.setInterfaceName(interfaceClass.getName());
                    request.setMethodName(method.getName());

                    Class<?>[] paramTypes = method.getParameterTypes();
                    String[] paramTypeNames = new String[paramTypes.length];
                    for (int i = 0; i < paramTypes.length; i++) {
                        paramTypeNames[i] = paramTypes[i].getName();
                    }
                    request.setParamTypeNames(paramTypeNames);
                    request.setParams(args);

                    // 2. 转成 JSON
                    String jsonReq = MAPPER.writeValueAsString(request);

                    // 3. 发送 POST 请求
                    HttpURLConnection conn = (HttpURLConnection) new java.net.URL(RPC_URL).openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    conn.setDoOutput(true);
                    // 增加超时，防止程序卡死
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);

                    try (OutputStream out = conn.getOutputStream()) {
                        out.write(jsonReq.getBytes(StandardCharsets.UTF_8));
                    }

                    // 4. 读取响应
                    int responseCode = conn.getResponseCode();
                    InputStream inputStream = (responseCode == 200)
                            ? conn.getInputStream()
                            : conn.getErrorStream();

                    // 5. 用 JsonNode 手动解析
                    JsonNode root = MAPPER.readTree(inputStream);
                    inputStream.close();
                    conn.disconnect();

                    // 检查业务状态码
                    int code = root.get("code").asInt();
                    if (code != 0) {
                        String errMsg = root.get("message").asText();
                        throw new RuntimeException("远程调用失败：" + errMsg);
                    }

                    // 取 data 字段，转成字符串返回
                    JsonNode dataNode = root.get("data");
                    if (dataNode == null || dataNode.isNull()) {
                        return null;
                    }
                    if (dataNode.isTextual()) {
                        return dataNode.asText();  // 如果 data 是字符串，直接返回
                    }
                    return dataNode.toString();     // 如果 data 是对象，转成 JSON 字符串
                }
        );
    }
}