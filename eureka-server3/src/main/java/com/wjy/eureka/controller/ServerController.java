/**
 * Copyright (C), 2015-2019, 上海象翌微链有限公司
 * FileName: ServerController
 * Author:   suneee
 * Date:     2019/2/11 16:17
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.wjy.eureka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author suneee
 * @create 2019/2/11
 * @since 1.0.0
 */
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
            eurekaDiscoveryClient.getInstances(serv).forEach(a -> System.out.println("*******"+a));
        }
    }
}
