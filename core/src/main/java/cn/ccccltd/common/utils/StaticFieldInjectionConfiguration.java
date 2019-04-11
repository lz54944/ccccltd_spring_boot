package cn.ccccltd.common.utils;

import javax.annotation.PostConstruct;

import cn.ccccltd.daos.ConfigDao;
import cn.ccccltd.jms.JMSTool;
import cn.ccccltd.jpa.JPAListener;
import cn.ccccltd.tool.ConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;

/**
 * 工具类的注入
 *
 * @author ccccltd
 */
@Configuration
public class StaticFieldInjectionConfiguration {

    @Autowired
    MessageSource resources;

    @Autowired
    ConfigDao configDao;

    @Autowired
    JMSTool jmsTool;

    @PostConstruct
    private void init() {
        System.out.println("\n\n-----StaticFieldInjectionConfiguration----\n\n");

        CheckUtil.setResources(resources);
        ConfigUtil.setConfigDao(configDao);

        JPAListener.setJmsTool(jmsTool);
    }
}