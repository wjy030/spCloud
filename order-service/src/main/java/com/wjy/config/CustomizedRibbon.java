/**
 * Copyright (C), 2015-2019, 上海象翌微链有限公司
 * FileName: CustomizedRibbon
 * Author:   suneee
 * Date:     2019/2/13 15:12
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.wjy.config;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author suneee
 * @create 2019/2/13
 * @since 1.0.0
 */
@Configuration
public class CustomizedRibbon {

    @Bean
    public IRule ribbonRule(IClientConfig config) {
        RandomRule rule = new RandomRule();
        return rule;
    }
}
