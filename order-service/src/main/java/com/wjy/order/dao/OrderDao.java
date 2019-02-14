/**
 * Copyright (C), 2015-2019, 上海象翌微链有限公司
 * FileName: OrderDao
 * Author:   suneee
 * Date:     2019/2/12 10:34
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.wjy.order.dao;

import com.wjy.order.model.dbo.OrderDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author suneee
 * @create 2019/2/12
 * @since 1.0.0
 */
public interface OrderDao extends JpaRepository<OrderDO,Integer> {

}
