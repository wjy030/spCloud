# Ribbon负载均衡
## 服务间常用调用方式
### RPC
* 远程过程调用，像调用本地服务(方法)一样调用服务器的服务
* 支持同步、异步调用
* 客户端和服务器之间建立TCP连接，可以一次建立一个，也可以多个调用复用一次链接
* RPC数据包很小
* RPC要处理的问题: 编解码，序列化，链接，丢包，协议
* 常用二进制协议:protobuf,thrift
### Rest(http)
* 支持多种协议和功能
* 开发方便成本低
* http数据包很大
## 负载均衡介绍
负载均衡分为服务端负载均衡和客户端负载均衡
### 服务端负载均衡
服务端负载均衡又分为硬件负载均衡和软件负载均衡
#### 硬件负载均衡
![硬件负载均衡](硬件负载均衡.png)
* 通过负载均衡硬件设备选择可用的peer节点
* 性能极其优异,成本昂贵
* 常用设备F5　　
#### 软件负载均衡
![软件负载均衡](软件负载均衡2.png)
* 负载均衡软件通过选定的策略选择可用的peer节点
* 性能尚可,成本便宜
* 常用软件Nginx,LVS等
### 客户端负载均衡
客户端软件维护服务端所有peer节点,根据指定策略选择可用的peer节点
## Ribbon负载均衡实现方式
1. 从注册中心根据spring.application.name获取其下所有集群节点实例
2. 根据默认或指定的策略(如轮询,随机等)选择一个节点实例
3. 取得节点实例信息,访问该服务实例
## 具体实现
### @LoadBalanced
```
@Bean
@LoadBalanced
public RestTemplate restTemplate() {
    return new RestTemplate();
}
```
```
 restTemplate.getForObject("http://product-service/product/findById/{1}",ProductDO.class,
                productId);
```                
加上了@LoadBalanced注解后,RestTemplate实例实际会通过LoadBalancerClient以负载均衡方式调用服务
### 直接使用LoadBalancerClient
相当于以自己的代码实现@LoadBalanced注解
```
 ServiceInstance serviceInstance = loadBalancerClient.choose("product-service");
String url = String.format("http://%s:%s/product/findById/{1}",serviceInstance.getHost(),serviceInstance
        .getPort());
RestTemplate template = new RestTemplate();
ProductDO productDO = template.getForObject(url, ProductDO.class, productId);
```
## 自定义负载均衡策略
因为是消费端的负载均衡,在消费端中配置.如果要配置应用名为*product-service*的服务的负载均衡策略为随机策略
```
product-service:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
```    
### 策略选择
1. 如果每个机器配置一样，则建议不修改策略 (推荐) 默认策略为轮询
2. 如果部分机器配置强，则可以改为 WeightedResponseTimeRule 权重
