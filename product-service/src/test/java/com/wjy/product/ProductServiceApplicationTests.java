package com.wjy.product;

import com.wjy.product.dao.ProductDao;
import com.wjy.product.model.dao.ProductDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceApplicationTests {

    @Resource
    private ProductDao productDao;
    @Test
    public void contextLoads() {
        productDao.save(new ProductDO("iphone",new BigDecimal(4000),10));
        productDao.save(new ProductDO("电脑",new BigDecimal(2000),8));
        productDao.save(new ProductDO("冰箱",new BigDecimal(1100),5));
        productDao.save(new ProductDO("空调",new BigDecimal(8000),10));
        productDao.save(new ProductDO("电视",new BigDecimal(3588),5));
    }

}

