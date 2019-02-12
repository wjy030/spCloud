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
## 负载均衡实现方式
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
