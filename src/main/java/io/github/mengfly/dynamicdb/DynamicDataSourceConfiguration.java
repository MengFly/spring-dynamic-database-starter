package io.github.mengfly.dynamicdb;

import io.github.mengfly.dynamicdb.initializer.DynamicDataSourceInitializer;
import io.github.mengfly.dynamicdb.initializer.PropertiesDataSourceInitializer;
import io.github.mengfly.dynamicdb.resolver.DynamicDataSourceResolver;
import io.github.mengfly.dynamicdb.resolver.PropertiesDataSourceResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author Mengfly
 */
@Configuration
public class DynamicDataSourceConfiguration {

    @Bean
    @Primary
    @Autowired
    public DynamicDataSource dataSource(DynamicDataSourceResolver resolver) throws Exception {
        return new DynamicDataSource(resolver);
    }

    @Bean
    @ConditionalOnMissingBean(DynamicDataSourceResolver.class)
    public DynamicDataSourceResolver resolver() {
        return new PropertiesDataSourceResolver();
    }

    @Bean
    @Autowired
    @ConditionalOnMissingBean(DynamicDataSourceInitializer.class)
    @ConditionalOnProperty(prefix = "spring.datasource", name = "enable-script", havingValue = "true")
    public PropertiesDataSourceInitializer propertiesDataSourceInitializer(DynamicDataSource dataSource) {
        PropertiesDataSourceInitializer initializer = new PropertiesDataSourceInitializer();
        initializer.setDataSource(dataSource);
        return initializer;
    }
}
