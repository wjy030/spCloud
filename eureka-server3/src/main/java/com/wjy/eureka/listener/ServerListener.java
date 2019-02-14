/**
 * Copyright (C), 2015-2019, 上海象翌微链有限公司
 * FileName: ServerListener
 * Author:   suneee
 * Date:     2019/2/11 14:04
 * Description: 事件监听
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.wjy.eureka.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.eureka.server.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 〈一句话功能简述〉<br> 
 * 〈事件监听〉
 *
 * @author suneee
 * @create 2019/2/11
 * @since 1.0.0
 */
@Component
public class ServerListener {
private static final Logger log = LoggerFactory.getLogger(ServerListener.class);
    @EventListener
    public void listen(EurekaInstanceRegisteredEvent event) {
        log.info(">>>>>>这是实例注册事件: instance:"+event.getInstanceInfo().getInstanceId());
    }

//    @EventListener
//    public void listen(EurekaInstanceRenewedEvent event) {
//        log.info(">>>>>>这是实例续约事件: instance:"+event.getInstanceInfo().getInstanceId());
//    }

    @EventListener
    public void listen(EurekaRegistryAvailableEvent event) {
        log.info(">>>>>>这是注册中心启动完毕事件: "+event.getSource());
    }

    @EventListener
    public void listen(EurekaServerStartedEvent event) {
        log.info(">>>>>>这是注册中心开始启动事件: "+event.getSource());
    }

    @EventListener
    public void listen(EurekaInstanceCanceledEvent event) {
        log.info(">>>>>>这是实例下线中心事件: "+event.getAppName());
    }
}
