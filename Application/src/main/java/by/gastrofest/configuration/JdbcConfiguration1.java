package by.gastrofest.configuration;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "by.gastrofest.repository.one",
        entityManagerFactoryRef = "entityManagerFactory1"
)
@EnableTransactionManagement
public class JdbcConfiguration1 {

    @Bean(name = "datasource1")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource1() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "entityManagerFactory1")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory1(
            @Qualifier("datasource1") DataSource dataSource) {
        return JdbcConfiguration2.getLocalContainerEntityManagerFactoryBean(dataSource);
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("entityManagerFactory1") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
