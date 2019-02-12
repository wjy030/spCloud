# Eureka 注册中心
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
**此为单例模式的配置,通常的配置是集群模式**  
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
**要注册服务供别人调用必须配置spring.application.name.别人通过这个值来调用服务如http://product-service/product/findById/{1}**
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
### System status
* Environment 环境名  对应主配置文件属性名: eureka.environment
* Data center 数据中心名  对应主配置文件属性名: eureka.datacenter
* Current time 当前时间
* Uptime 启动过了多久
* Lease expiration enabled
* Renews threshold server期望在每分钟中收到的心跳次数 **client个数为n，阈值为1+2*n（此为一个server且禁止自注册的情况）**
* Renews (last min) 上一分钟内收到的心跳次数
### DS Replicas
集群相关配置
### Instances currently registered with Eureka
Eureka实例注册列表
* Application 客户端应用名
* Availability Zones 该应用下所有可获取的客户端的域/ip的数量
* Status 客户端的状态（是否在运行） 可获取的域地址
### General Info
概况信息
* total-avail-memory 所有可用内存
* environment 环境名
* num-of-cpus cpu数
* current-memory-usage  已使用内存
* server-uptime  服务已启动多久
* registered-replicas 集群相关，已注册的服务节点。这些节点间会有数据同步
* unavailable-replicas  集群相关 不可用节点
* available-replicas  集群相关 可用节点
### Instance Info
当前注册中心服务的实例信息
* ipAddr 实例的ip
* status 实例的状态（是否可运行）
## 控制台访问控制
```
eureka:
  dashboard:
      enabled: true
      path: /dashbord
```
* eureka.dashboard.enabled 控制是否能访问注册中心控制台,false就不能访问,默认true
* eureka.dashboard.path 注册中心访问根路径设置,默认为/,设置为/dashbord 则访问路径为 http://localhost:8761/dashbord
## Eureka的自我保护模式相关特性,概念
### Eureka概念
1. Register: 服务注册  
当Eureka client 向 Eureka server 注册时,会提供自身的元数据,比如IP地址,端口号,运行状况等等  
2. Renew: 服务续约  
Eureka client每隔30秒向Eureka server发送一次心中来续约,表示自己仍在正常运行. 默认情况下,**在未进入自我保护模式时**,当server在90秒内
未收到续约时就会从注册表剔除该client实例  
3. Cancel: 服务注销/下线  
Eureka client在程序关闭时会向Eureka server发送注销请求,server收到请求后会从注册表剔除该client.该请求不会自动完成,通过调用
DiscoveryManager.getInstance().showDownComponent()实现
4. Fetch Registries: 获取注册表
Eureka client会从Eureka server获取注册表信息并缓存在本地并默认每隔30秒更新一次注册表,缓存信息与服务端注册表不同时client会自动处理,
如果因为某些原因注册表不能及时匹配,client会重新获取整个注册表.Eureka client和server之间通过JSON/XML相互通信,server缓存并压缩着注册
表及每个应用程序的信息.client和server之间默认通过压缩的json来交互.
5. Eviction: 服务剔除
非自我保护模式下,当Eureka server在90秒内没有接收到来自Eureka client的心跳时,会将该client的服务实例从注册表中剔除,即服务剔除
### Eureka特性
1. 当注册中心挂了,Eureka客户端与之间仍可以相互调用,当注册中心重启后,客户端会陆续注册进来
2. 当服务提供者挂了,**在关闭自我保护模式下**,注册中心在规定时间(默认60s)移除客户端
3. 自我保护,当一个Eureka服务器出现时,尝试从相邻Peer节点(即集群中的节点)获取注册列表.如果获取注册列表出现问题,会尝试从其他Peer节点
获取注册列表.如果Eureka服务器成功获取了所有服务实例,则会根据该信息计算出更新阈值(Renews threshold),当规定时间(默认15分钟)内服务器
接收到的续约低于该阈值的指定百分比(默认85%),则会开启自我保护模式,即不再剔除注册列表信息.
### 主文件中的自我保护相关配置
```
eureka:
  server:
    enable-self-preservation: true
    renewal-percent-threshold: 0.85
    renewal-threshold-update-interval-ms: 6000
    eviction-interval-timer-in-ms: 5000
    delta-retention-timer-interval-in-ms: 30000
```
* enable-self-preservation: 是否启用自我保护模式 默认true
* renewal-percent-threshold: 指定一个百分比,默认为0.85,当规定时间内Eureka server收到的续约低于**Renews threshold * 指定百分比**时
会开启自我保护模式
* renewal-threshold-update-interval-ms: 指定更新续约阈值的时间,单位微秒,Eureka server每隔指定的时间会重新计算续约阈值,默认值15*60*1000
* eviction-interval-timer-in-ms: 指定剔除服务的间隔时间,单位微秒,Eureka server根据指定间隔定期剔除没有续约的服务
* delta-retention-timer-interval-in-ms: 指定清理任务程序被唤醒的时间间隔,清理过期的增量信息,单位为毫秒,默认为30 * 1000
## 高可用
```
eureka:
  instance:
    hostname: peer3
    appname: server
    prefer-ip-address: true
    instance-id: ${eureka.instance.appname}:${server.port}
  client:
    serviceUrl:
      defaultZone: http://peer2:8762/eureka/,http://arthas:123456@peer1:8761/eureka/
spring:
  profiles: peer3
```
* 关键点:**在每一个集群节点中的defaultZone配置其他的集群节点,以 , 分隔**
* **client每个集群节点的spring.application.name要是相同的,以实现负载均衡调用**
* 其他相关配置:appname,instance-id,prefer-ip-address
* prefer-ip-address 默认false,配置true后在向其他服务器注册时会提供ip而不是机器名
![配置](pp.png)
图中 点击server:8762后,当prefer-ip-address=false,会跳转到``http://suneee-PC:8762/actuator/info``,其中sunee-PC是机器名.
当设置为true时,会跳转到``http://172.19.14.159:8763/actuator/info``  
**当eureka client向eureka server注册时只要在defaultZone配置任一peer节点,所有节点都会被注册.但是当该服务down掉时,client也就down掉了.因此
应该在client的defaultZone中配置server的所有peer节点**
## Eureka编程
在Eureka服务端可以通过编程做些监听或查询的工作
### Eureka的监听事件
通过Spring的事件ApplicationEvent及监听@EventListener实现  
#### spring事件简介
* 通过继承ApplicationEvent创建spring事件``public class EmailEvent extends ApplicationEvent {``
* 通过@EventListener实现对该事件的监听
```
@EventListener
public void listen(EmailEvent emailEvent) {
    emailEvent.print();
}
```
* 通过ApplicationContext发布事件,相应的监听方法就会被调用
```
EmailEvent event = new EmailEvent("hello","boylmx@163.com","this is a email text!");
context.publishEvent(event);
```
#### Eureka的事件元素
* EurekaInstanceRegisteredEvent 实例注册事件,每当接收到一个eureka实例的注册就会触发
* EurekaInstanceRenewedEvent 实例续约事件,每当接收到一个续约就会触发
* EurekaRegistryAvailableEvent 注册中心启动完毕事件,注册中心启动完毕时会触发,此时注册中心是可得到的
* EurekaServerStartedEvent Eureka服务器启动完毕事件,Eureka服务器启动完毕时会触发
* EurekaInstanceCanceledEvent 实例下线事件,每当有一个实例下线时会触发
```
@Component
public class ServerListener {
private static final Logger log = LoggerFactory.getLogger(ServerListener.class);
    @EventListener
    public void listen(EurekaInstanceRegisteredEvent event) {
        log.info(">>>>>>这是实例注册事件: instance:"+event.getInstanceInfo().getInstanceId());
    }

    @EventListener
    public void listen(EurekaInstanceRenewedEvent event) {
        log.info(">>>>>>这是实例续约事件: instance:"+event.getInstanceInfo().getInstanceId());
    }

    @EventListener
    public void listen(EurekaRegistryAvailableEvent event) {
        log.info(">>>>>>这是注册中心启动完毕事件: "+event.getSource());
    }

    @EventListener
    public void listen(EurekaServerStartedEvent event) {
        log.info(">>>>>>这是Eureka服务器启动完毕事件: "+event.getSource());
    }

    @EventListener
    public void listen(EurekaInstanceCanceledEvent event) {
        log.info(">>>>>>这是实例下线中心事件: "+event.getAppName());
    }
}
```
#### @EnableDiscoveryClient与@EnableEurekaClient
1. @EnableDiscoveryClient注解是基于spring-cloud-commons依赖，不针对eureka 
2. @EnableEurekaClient注解是基于spring-cloud-starter-netflix-eureka-client依赖，只能为eureka作用；
3. **从Spring Cloud Edgware开始，@EnableDiscoveryClient 或@EnableEurekaClient 可省略。只需加上相关依赖，并进行相应配置，即可将微服务注册到服务发现组件上。**
#### EurekaDiscoveryClient类与DiscoveryClient类
DiscoveryClient类是更高级的抽象,优点与上面相同,可以通过该类查找服务实例相关信息
```
@Controller
public class ServerController {

    @Resource
    private DiscoveryClient discoveryClient;
    @Resource
    private EurekaDiscoveryClient eurekaDiscoveryClient;

    @GetMapping("/info")
    @ResponseBody
    public void info() {
        for(String serv: discoveryClient.getServices()) {
            System.out.println("--------"+serv);
            System.out.println(eurekaDiscoveryClient.description());
            eurekaDiscoveryClient.getInstances(serv).forEach(a -> System.out.println("*******"+a));
        }
    }
}
```
