/**
 * Copyright (C), 2015-2019, 上海象翌微链有限公司
 * FileName: ProductDao
 * Author:   suneee
 * Date:     2019/1/9 15:48
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.wjy.product.dao;

import com.wjy.product.model.dao.ProductDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author suneee
 * @create 2019/1/9
 * @since 1.0.0
 */
public interface ProductDao extends JpaRepository<ProductDO,Integer> {

}
