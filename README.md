# 简易 HTTP-JSON RPC 框架
基于 **JDK 动态代理 + HTTP + JSON + SpringBoot** 手写实现的极简 RPC 框架。
无需复杂注册中心、无需自定义协议，通过 HTTP POST 完成远程方法调用，从零开始实现**服务注册、动态代理、网络通信、反射调用**适合 RPC 原理学习参考。

## 项目亮点
- **零配置、轻量级**：基于原生 HttpURLConnection + Jackson 实现
- **透明远程调用（动态代理）**：基于 JDK 动态代理封装网络请求，客户端像调用本地方法一样调用远程服务
- **支持多参数、基本数据类型**：完美解决 int/long 等基本类型反射匹配问题
- **支持方法重载**：通过「方法名 + 参数类型数组」精准匹配重载方法
- **统一请求响应规范**：自定义 `RpcRequest`、`RpcResponse`，统一异常处理
- **内置简易服务注册中心**：内存 Map 实现服务注册与发现

## 核心原理
### 整体调用链路
1. 客户端通过 **JDK 动态代理** 拦截接口方法调用
2. 代理层封装 `interfaceName、methodName、参数类型、参数值` 为 `RpcRequest`
3. Jackson 序列化为 JSON，通过 **HTTP POST** 发送至服务端
4. SpringBoot 服务端接收请求，通过接口全类名获取服务实例
5. 将字符串类型的参数类型，还原为真实 Class 类型，反射匹配目标方法
6. 反射执行目标方法，结果封装为 `RpcResponse` 返回客户端
7. 客户端解析响应数据，完成远程调用

## 技术栈
- Java 17
- SpringBoot 2.7.14
- JDK 动态代理
- HTTP（Tomcat）
- Jackson（JSON 序列化/反序列化）
- Java 反射机制
- 
## 项目结构
```
rpc/
├── src/main/java/com/yg/
│ ├── api/ # 服务接口定义
│ │ └── UserService.java
│ ├── common/ # 公共模块
│ │ ├── RpcRequest.java
│ │ └── RpcResponse.java
│ ├── provider/ # 服务提供者
│ │ ├── RpcServer.java # 服务注册表
│ │ └── UserServiceImpl.java
│ ├── consumer/ # 服务消费者
│ │ ├── RpcProxyFactory.java # 动态代理工厂
│ │ └── ClientTest.java # 测试入口
│ ├── controller/ # HTTP 入口
│ │ └── RpcController.java
│ └── Application.java # Spring Boot 启动类
├── pom.xml
└── README.md
```

## 运行方式
1. **启动服务端**：运行 `Application.java`，自动注册 UserService 服务
2. **运行客户端**：执行 `ClientTest.java`
3. 测试两类调用：
   - 单参数方法：`sayHello(String name)`
   - 多类型参数方法：`getUserInfo(String name, int age, String city)`

## 核心流程
```
客户端
  │
  ├── 调用代理对象方法（如 userService.getUserInfo）
  │
  ├── 动态代理拦截，封装 RpcRequest（接口名 + 方法名 + 参数类型 + 参数值）
  │
  ├── 序列化为 JSON，发送 HTTP POST 请求到服务端
  │
服务端
  │
  ├── RpcController 接收请求，反序列化为 RpcRequest
  │
  ├── 根据 interfaceName 从 RpcServer 获取服务实例
  │
  ├── 根据 methodName + paramTypeNames 反射找到 Method
  │
  ├── 将参数值转换为目标类型（convertValue）
  │
  ├── 调用 method.invoke()，执行真正的业务逻辑
  │
  └── 封装 RpcResponse，返回 JSON 给客户端
```

## 已解决核心难点
- **解决 Jackson 无法序列化 Class 数组问题**：不传输 Class 对象，改为传输类名字符串，服务端动态还原
- **解决基本类型反射报错**：单独处理 int/long/boolean 等八大基本类型，兼容 `Class.forName` 无法加载基本类型的问题
- **解决 HTTP 响应乱码、400 报错**：统一 UTF-8 编码、区分正常流/错误流读取
- **解决重载方法匹配失败**：精准通过参数类型数组匹配方法

## 版本演进
v1.0.0-json-rpc：基于 HTTP + JSON 的完整 RPC 实现，支持多参数和基本类型

## 项目缺陷 & 后续优化方向
- 通信方式为 HTTP，性能较低，后续可替换为 TCP 自定义协议
- 服务注册为内存 Map，不支持集群，后续可接入 Nacos/ZooKeeper 实现注册中心
- 无负载均衡机制，后续可增加随机、轮询负载均衡策略
- 无超时重试、熔断降级机制
- 暂未支持复杂 POJO 对象传参，仅支持基础类型与字符串

## 项目总结
本项目完整复刻了 RPC 框架的核心思想：**代理屏蔽网络细节、序列化传输数据、服务注册发现、反射远程调用**。抛开复杂的网络框架和注册中心，专注 RPC 底层原理，是非常优质的 Java 进阶、面试实战项目。数据、服务注册发现、反射远程调用。抛开复杂的网络框架和注册中心，专注 RPC 底层原理，是非常优质的 Java 进阶、面试实战项目。

## 最后
这个项目是我学习 RPC 原理时手写的框架原型。从最开始的单参数调用，到现在的多参数 + 类型自动转换，每一步都加深了我对动态代理、反射、序列化和网络通信的理解。如果你也在学习 RPC，欢迎 clone 下来一起交流。
