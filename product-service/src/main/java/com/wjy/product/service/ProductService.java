/**
 * Copyright (C), 2015-2019, 上海象翌微链有限公司
 * FileName: ProductService
 * Author:   suneee
 * Date:     2019/1/9 15:49
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.wjy.product.service;

import com.wjy.product.dao.ProductDao;
import com.wjy.product.model.dao.ProductDO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author suneee
 * @create 2019/1/9
 * @since 1.0.0
 */
@Service
public class ProductService {
    @Value("${server.port}")
    private String port;
    @Resource
    private ProductDao productDao;

    public List<ProductDO> listProduct() {
        return productDao.findAll();
    }

    public ProductDO findById(int id) {
        ProductDO productDO = productDao.findById(id).get();
        productDO.setName(productDO.getName()+" data from port:"+port);
        return productDO;
    }

    public void save(ProductDO productDO) {
        productDao.saveAndFlush(productDO);
    }
}
