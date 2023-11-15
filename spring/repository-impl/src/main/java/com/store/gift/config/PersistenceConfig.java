package com.store.gift.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

/**
 * Configuration class for persistence-related beans.
 * <p>
 * This annotation indicates that the class is a configuration class that defines Spring beans.
 * It is used to replace traditional XML-based configuration.
 * <p>
 * This {@link EnableTransactionManagement} annotation enables Spring's transaction management capabilities,
 * allowing the use of declarative transactions in the application.
 */
@Configuration
@EnableTransactionManagement
public class PersistenceConfig implements ApplicationContextAware {

    /**
     * The application context used to obtain classpath resources.
     * This class implements the {@link ApplicationContextAware} interface
     * to be aware of the application context.
     */
    private ApplicationContext applicationContext;
    /**
     * An array of paths to the SQL script files for initialization.
     */
    @Value("${spring.sql.init.data}")
    private String[] initDataScriptPaths;
    /**
     * The property for showing SQL statements.
     */
    @Value("${spring.jpa.show-sql}")
    private String showSql;

    /**
     * The database URL.
     */
    @Value("${spring.datasource.url}")
    private String url;

    /**
     * The database username.
     */
    @Value("${spring.datasource.username}")
    private String username;

    /**
     * The database password.
     */
    @Value("${spring.datasource.password}")
    private String password;

    /**
     * The Hibernate DDL auto property.
     */
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String hibernateDdlAuto;

    /**
     * The database driver class name.
     */
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    /**
     * The Hibernate dialect property.
     */
    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String dialect;

    /**
     * Creates a bean for Jackson2ObjectMapperBuilder.
     *
     * @return the Jackson2ObjectMapperBuilder bean
     */
    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder();
    }

    /**
     * Creates an {@link EntityManager} bean.
     *
     * @param factoryBean the {@link LocalContainerEntityManagerFactoryBean} bean.
     * @return the created {@link EntityManager} bean.
     * @throws IllegalStateException if the entity manager factory
     *                               is not initialized.
     */
    @Bean
    public EntityManager entityManager(
            final LocalContainerEntityManagerFactoryBean factoryBean) {
        return Optional.ofNullable(factoryBean.getObject())
                .map(EntityManagerFactory::createEntityManager)
                .orElseThrow(() -> new IllegalStateException(
                        "Entity manager factory is not initialized"));
    }

    /**
     * Creates a {@link DataSourceInitializer} bean for executing database scripts.
     *
     * @param dataSource the {@link DataSource} bean.
     * @return the created {@link DataSourceInitializer} bean.
     */
    @Bean
    public DataSourceInitializer dataSourceScriptInitializer(final DataSource dataSource) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        Arrays.stream(initDataScriptPaths)
                .map(scriptPath -> applicationContext.getResource(scriptPath))
                .forEach(populator::addScript);
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(populator);
        return initializer;
    }

    /**
     * Creates a {@link DataSource} bean.
     *
     * @return the created {@link DataSource} bean.
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    /**
     * Creates a {@link LocalContainerEntityManagerFactoryBean}
     * bean for managing the entity manager factory.
     *
     * @param dataSource the {@link DataSource} bean.
     * @return the created {@link LocalContainerEntityManagerFactoryBean} bean.
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            final DataSource dataSource) {
        Properties properties = new Properties();
        properties.setProperty("hibernate.show_sql", showSql);
        properties.setProperty("hibernate.hbm2ddl.auto", hibernateDdlAuto);
        properties.setProperty("hibernate.dialect", dialect);
        properties.setProperty("hibernate.default_batch_fetch_size", "16");
        LocalContainerEntityManagerFactoryBean factoryBean =
                new LocalContainerEntityManagerFactoryBean();
        factoryBean.setJpaProperties(properties);
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("com.store.gift.entity");
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return factoryBean;
    }

    /**
     * Creates a {@link PlatformTransactionManager} bean for managing transactions.
     *
     * @param factoryBean the {@link LocalContainerEntityManagerFactoryBean} bean.
     * @return the created {@link PlatformTransactionManager} bean.
     */
    @Bean
    public PlatformTransactionManager transactionManager(
            final LocalContainerEntityManagerFactoryBean factoryBean) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(factoryBean.getObject());
        return transactionManager;
    }

    /**
     * Set the ApplicationContext that this object runs in.
     * Normally this call will be used to initialize the object.
     * <p>
     * Invoked after population of normal bean properties but before an init callback
     *
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws BeansException if thrown by application context methods
     */
    @Override
    public void setApplicationContext(
            final @NonNull ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }
}
