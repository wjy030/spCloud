/**
 * Copyright (C), 2015-2019, 上海象翌微链有限公司
 * FileName: OrderService
 * Author:   suneee
 * Date:     2019/2/12 10:36
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.wjy.order.service;

import com.wjy.order.dao.OrderDao;
import com.wjy.order.model.dbo.OrderDO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author suneee
 * @create 2019/2/12
 * @since 1.0.0
 */
@Service
public class OrderService {

    @Resource
    private OrderDao orderDao;

    public void save(OrderDO orderDO) {
        orderDao.save(orderDO);
    }
}
