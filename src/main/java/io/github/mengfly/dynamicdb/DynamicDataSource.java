package io.github.mengfly.dynamicdb;

import io.github.mengfly.dynamicdb.resolver.DynamicDataSourceResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mengfly
 */
@Component
public class DynamicDataSource extends AbstractRoutingDataSource {

    static final Map<Object, Object> DATA_SOURCE_MAP = new ConcurrentHashMap<>(10);

    @Value("${spring.datasource.name:defaultDataSource}")
    private String defaultDataSourceName;
    private boolean initialized = false;
    private final DynamicDataSourceResolver resolver;

    public DynamicDataSource(DynamicDataSourceResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void afterPropertiesSet() {
        if (!initialized) {
            initialized = true;
            try {
                DATA_SOURCE_MAP.putAll(resolver.loadDataSource());
                DataSource defaultTargetDataSource = resolver.defaultDataSource();
                DATA_SOURCE_MAP.put(defaultDataSourceName, defaultTargetDataSource);
                setTargetDataSources(DATA_SOURCE_MAP);
                setDefaultTargetDataSource(defaultTargetDataSource);
            } catch (Exception e) {
                throw new RuntimeException("Initial data source failed", e);
            }
        }
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceHelper.dataSourceKey();
    }

    public void addDataSource(String dataSourceKey, DataSource dataSource) {
        DATA_SOURCE_MAP.put(dataSourceKey, dataSource);
        afterPropertiesSet();
    }

    public Set<Object> listDataSourceName() {
        return DATA_SOURCE_MAP.keySet();
    }
}
