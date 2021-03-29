package com.space.analysis.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * druid配置
 */

@Configuration
public class DruidConfig {
    /**
     * 获取配置文件配置 ,将druid数据源注入ioc容器
     */
    @ConfigurationProperties("spring.datasource")
    @Bean
    public DataSource druid() {
        return DruidDataSourceBuilder.create().build();
    }


    /**
     * 配置druid后台监听，配置管理后台Servlet
     */
    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        Map<String, String> initParam = new HashMap<>();
        //druid后台用户名
        initParam.put("loginUsername", "admin");
        //druid后台密码
        initParam.put("loginPassword", "admin");
        //默认就是允许所有访问
        initParam.put("allow", "");
        // 是否可以重置数据
        initParam.put("resetEnable", "false");

        bean.setInitParameters(initParam);
        return bean;
    }

    /**
     * 配置监听的filter
     */
    @Bean
    public FilterRegistrationBean druidStatFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());
        Map<String, String> initParam = new HashMap<>();
        initParam.put("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        bean.setInitParameters(initParam);
        bean.setUrlPatterns(Arrays.asList("/*"));
        return bean;
    }


}
