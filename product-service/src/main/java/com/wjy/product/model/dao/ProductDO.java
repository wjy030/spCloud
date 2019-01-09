/**
 * Copyright (C), 2015-2019, 上海象翌微链有限公司
 * FileName: ProductDO
 * Author:   suneee
 * Date:     2019/1/9 15:43
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.wjy.product.model.dao;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author suneee
 * @create 2019/1/9
 * @since 1.0.0
 */
@Entity
@Table(name = "sc_product")
public class ProductDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private BigDecimal price;
    private int store;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStore() {
        return store;
    }

    public void setStore(int store) {
        this.store = store;
    }

    public ProductDO(String name, BigDecimal price, int store) {
        this.name = name;
        this.price = price;
        this.store = store;
    }
    public ProductDO(){};
}
