package cn.ccccltd.springconfigs;

/**
 * Created by ccccltd on 2019/3/29.
 */
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "impalaEntityManagerFactory" ,
        transactionManagerRef = "impalaTransactionManager" ,
        basePackages = {"cn.ccccltd.impaladaos"}
)
public class ImpalaDBConfig {
    @Autowired
    private JpaProperties jpaProperties ;

    @Bean(name = "impalaDataSource")
    @ConfigurationProperties(prefix = "impala.datasource")
    public DataSource dataSource(@Autowired @Qualifier("impalaDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean(name="impalaDataSourceProperties")
    @ConfigurationProperties("impala.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean(name = "impalaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean
    entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("impalaDataSource") DataSource dataSource , Environment environment
    ) {

        LocalContainerEntityManagerFactoryBean impala = builder
                .dataSource(dataSource)
                .packages("cn.ccccltd.impalabeans")
                .persistenceUnit("impala")
                .build();

        Properties properties = new Properties();
        properties.setProperty("hibernate.physical_naming_strategy",
                "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
        impala.setJpaProperties(properties);
        return impala ;
    }


    @Bean(name = "impalaTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("impalaEntityManagerFactory") EntityManagerFactory
                    entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}