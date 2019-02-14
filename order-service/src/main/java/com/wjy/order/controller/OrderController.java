/**
 * Copyright (C), 2015-2019, 上海象翌微链有限公司
 * FileName: OrderController
 * Author:   suneee
 * Date:     2019/2/12 10:39
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.wjy.order.controller;

import com.wjy.order.model.dbo.OrderDO;
import com.wjy.order.model.dbo.ProductDO;
import com.wjy.order.service.OrderService;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author suneee
 * @create 2019/2/12
 * @since 1.0.0
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private RestTemplate restTemplate;
    @Resource
    private OrderService orderService;
    @Resource
    private LoadBalancerClient loadBalancerClient;
    @PostMapping("/save")
    public Object save(int userId, int productId) {

        OrderDO orderDO = new OrderDO();
//        ProductDO productDO = restTemplate.getForObject("http://product-service/product/findById/{1}",ProductDO.class,
//                productId);
        ServiceInstance serviceInstance = loadBalancerClient.choose("product-service");
        String url = String.format("http://%s:%s/product/findById/{1}",serviceInstance.getHost(),serviceInstance
                .getPort());
        RestTemplate template = new RestTemplate();
        ProductDO productDO = template.getForObject(url, ProductDO.class, productId);
        orderDO.setCreateTime(new Date());
        orderDO.setPrice(productDO.getPrice());
        orderDO.setProductName(productDO.getName());
        orderDO.setUserId(userId);
        orderDO.setTradeNo(UUID.randomUUID().toString());
//        orderService.save(orderDO);
        return orderDO;
    }
}
