package io.github.mengfly.dynamicdb;

import io.github.mengfly.dynamicdb.resolver.DynamicDataSourceResolver;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mengfly
 */
@Component
public class DynamicDataSource extends AbstractRoutingDataSource {

    private final Map<Object, Object> DATA_SOURCE_MAP = new ConcurrentHashMap<>(10);

    public DynamicDataSource(DynamicDataSourceResolver resolver) throws Exception{
        DATA_SOURCE_MAP.putAll(resolver.loadDataSource());
        DataSource defaultTargetDataSource = resolver.defaultDataSource();
        DATA_SOURCE_MAP.put(DynamicDataSourceHelper.DEFAULT_DATASOURCE_NAME, defaultTargetDataSource);
        setTargetDataSources(DATA_SOURCE_MAP);
        setDefaultTargetDataSource(defaultTargetDataSource);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceHelper.dataSourceKey();
    }

    public void addDataSource(String dataSourceKey, DataSource dataSource) {
        DATA_SOURCE_MAP.put(dataSourceKey, dataSource);
        afterPropertiesSet();
    }
}
