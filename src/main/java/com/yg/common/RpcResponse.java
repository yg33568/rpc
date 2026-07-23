package com.yg.common;

import java.io.Serializable;

public class RpcResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    // 状态码：0表示成功，非0表示失败
    private int code;
    // 错误信息（成功时为空）
    private String message;
    // 实际返回的数据
    private Object data;

    // 无参构造（Jackson 反序列化需要）
    public RpcResponse() {}

    // 全参构造（方便使用）
    public RpcResponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 静态方法：快速创建一个成功响应
    public static RpcResponse success(Object data) {
        return new RpcResponse(0, "success", data);
    }

    // 静态方法：快速创建一个失败响应
    public static RpcResponse error(String message) {
        return new RpcResponse(-1, message, null);
    }

    // getter/setter
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
}