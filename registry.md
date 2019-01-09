# 注册中心
## 注册中心的选择
* Zookeeper: CP 设计,保证了一致性,集群搭建的时候有个leader节点.leader节点失效会自动重新选举leader节点,
或者半数以上节点不可用,则无法提供服务,因此可用性没法满足
* Eureka: AP原则,无主从节点,一个节点挂了,自动切换其他节点可以使用,去中心化
## 注册中心原理
![原理](registry.png)
## Eureka 服务端搭建
在spring boot模块中引入Eureka Server启动器
```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```
在spring boot 主启动器加上@EnableEurekaServer注解
```
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
```
主配置文件基本配置  
此为单例模式的配置,通常的配置是集群模式  
```
server:
  port: 8761   //服务端口
eureka:
  instance:
    hostname: localhost   //服务实例域名/主机名/ip
  client:
    registerWithEureka: false    //表示是否注册自身到eureka服务器，因为当前这个应用就是eureka服务器，没必要注册自身，所以这里是false
    fetchRegistry: false    //表示是否从eureka服务器获取注册信息，同上，这里不需要
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/     //设置eureka服务器所在的地址，查询服务和注册服务都需要依赖这个地址。eureka/是固定值
```
## Eureka 客户端搭建
在spring boot模块中引入Eureka Client启动器
```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```
主配置文件基本配置
```
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/  //设置eureka服务器所在的地址,eureka客户端在这个地址注册和查询服务
  instance:
    appname: product-service  //设置分布式系统当前模块的应用名称
```
