/**
 * Copyright (C), 2015-2019, 上海象翌微链有限公司
 * FileName: ProductController
 * Author:   suneee
 * Date:     2019/1/9 15:53
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.wjy.product.controller;

import com.wjy.product.model.dao.ProductDO;
import com.wjy.product.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
@Controller
@RequestMapping("/product")
public class ProductController {

    @Resource
    private ProductService productService;
    @GetMapping("/list")
    @ResponseBody
    public List<ProductDO> listProduct() {
        return productService.listProduct();
    }
    @GetMapping("/findById/{id}")
    @ResponseBody
    public ProductDO findById(@PathVariable("id") int id) {
        return productService.findById(id);
    }
}
