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
### registerWithEureka,fetchRegistry,defaultZone属性作用
* 对于Erueka客户端,通过这三个属性连接Erueka服务地址(defaultZone是默认的服务地址)注册自己的服务,以及定时获取所有已注册的服务列表.
* 对于Erueka服务端,通过这三个属性连接其他Erueka服务器,与其同步注册的服务. 在单例模式下,registerWithEureka,fetchRegistry需要关闭,defaultZone可以
不用设置
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
## Eureka安全控制
引入安全控制后Eureka注册中心会启用密码认证,登录注册中心时需要输入用户名密码
### 引入
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```
引入后就启用安全控制,默认用户名为user,密码在启动时会心uuid方式生成并输出在日志中
#### 引入后要取消安全控制
旧版本在主配置文件上可以配置
```
security:
  basic:
    enabled: false 
```
该配置默认为true,可以改为false.**新版本该配置已不可用**  
新版本取消安全控制的方法:   
在主启动类或任意配置类上加上注解  
```
@EnableAutoConfiguration(exclude = {
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
```
### 指定用户名密码
在主配置文件中
```
spring:
  security:
    user:
      name: arthas
      password: 123456
```
### 服务注册与调用
* 加了安全控制后,除了访问Eureka服务器需要登录外,Eureka客户端连接服务器时也要用到用户名密码:
```
eureka:
  client:
    serviceUrl:
      defaultZone: http://arthas:123456@localhost:8761/eureka/
```
* 消费者要调用服务者的服务时,也使用用户名密码
### 新版本security取消csrf
新版本security启用了csrf,这样Eureka客户端即使用了用户名密码,在连接服务器地址时仍然会报错Cannot execute request on any known server  
要取消启用csrf就要添加代码
```
@EnableWebSecurity
public class EurekaSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        super.configure(http);
    }
}
```
## Eureka注册中心界面介绍
![界面](dashbord.png)
