package io.github.mengfly.dynamicdb;

import io.github.mengfly.dynamicdb.resolver.DynamicDataSourceResolver;
import io.github.mengfly.dynamicdb.resolver.PropertiesDataSourceResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
    @ConditionalOnMissingBean
    public DynamicDataSourceResolver resolver() {
        return new PropertiesDataSourceResolver();
    }
}
