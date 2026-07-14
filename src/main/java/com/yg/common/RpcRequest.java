package com.yg.common;

import java.io.Serializable;

/**
 * interfaceName：接口全限定名
 * methodName：要调用的方法名
 * paramTypes：方法每个入参对应的 Class 数组（解决多类型、重载匹配关键）
 * params：实际传入的参数值数组
 */
public class RpcRequest implements Serializable {
    private String interfaceName;
    private String methodName;
    private Class<?>[] paramTypes;
    private Object[] params;

    public RpcRequest() {
    }

    public RpcRequest(String interfaceName, String methodName, Object[] params, Class<?>[] paramTypes) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.params = params;
        this.paramTypes = paramTypes;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }
}
