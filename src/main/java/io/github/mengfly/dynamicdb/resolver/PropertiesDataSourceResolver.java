package io.github.mengfly.dynamicdb.resolver;

import io.github.mengfly.dynamicdb.DatasourceProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.DataBinder;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 该数据源加载器从配置文件中加载数据源
 * <p>
 * 该加载器默认会将spring.datasource下面的数据源作为默认数据源，将spring.datasource.targets下面的数据源列表作为目标数据源，配置文件中的配置内容如下
 * <blockquote><pre>
 * spring:
 *   datasource:
 *     # 默认数据源配置
 *     url: {url}
 *     username: {username}
 *     password: {password}
 *     drive-class-name: {driveClassName}
 *     targets:
 *       - id: {ds0}
 *         username: {username}
 *         password: {password}
 *         drive-class-name: {driveClassName}
 *       - id: {ds1}
 *         ......
 * </pre></blockquote>
 *
 * @author Mengfly
 */
@Component
public class PropertiesDataSourceResolver implements DynamicDataSourceResolver, EnvironmentAware {

    private static final Log logger = LogFactory.getLog(PropertiesDataSourceResolver.class);

    private static final String PROPERTIES_DATASOURCE_TARGETS = "spring.datasource.targets";
    private static final String PROPERTIES_DATASOURCE_CONFIG = "spring.datasource";
    private static final String[] IGNORE_DATASOURCE_PROPERTIES = {
            "username", "password", "driver-class-name", "url", "type", "targets"
    };

    private Binder binder;
    private PropertyValues datasourceConfiguration;

    @Override
    public Map<Object, Object> loadDataSource() throws Exception {
        datasourceConfiguration = readDatasourceConfiguration();
        Bindable<List<DatasourceProperties>> targetProperties = Bindable.listOf(DatasourceProperties.class);
        List<DatasourceProperties> datasourceProperties = binder.bind(PROPERTIES_DATASOURCE_TARGETS, targetProperties).get();
        if (datasourceProperties.isEmpty()) {
            logger.warn("Dynamic database can't found any targets database config, " +
                    "If your project just have one datasource, It's not recommend to use this framework, " +
                    "If not, please put datasource list config at spring.datasource.targets");
            return Collections.emptyMap();
        }
        Map<Object, Object> targetDatasourceMap = new LinkedHashMap<>();
        for (DatasourceProperties datasourceProperty : datasourceProperties) {
            final DataSource dataSource = datasourceProperty.newDataSource();
            DataBinder binder = new DataBinder(dataSource);
            binder.bind(datasourceConfiguration);
            targetDatasourceMap.put(datasourceProperty.getId(), dataSource);
        }
        return targetDatasourceMap;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.binder = Binder.get(environment);
    }

    @Override
    public DataSource defaultDataSource() throws Exception {
        DatasourceProperties defaultDataSource = binder.bind(PROPERTIES_DATASOURCE_CONFIG, DatasourceProperties.class).get();
        DataSource dataSource = defaultDataSource.newDataSource();
        DataBinder binder = new DataBinder(dataSource);
        binder.bind(datasourceConfiguration);
        return dataSource;
    }

    private PropertyValues readDatasourceConfiguration() {
        Map<?, ?> configuration = binder.bind(PROPERTIES_DATASOURCE_CONFIG, Map.class).get();
        for (String ignoreDatasourceProperty : IGNORE_DATASOURCE_PROPERTIES) {
            configuration.remove(ignoreDatasourceProperty);
        }
        return new MutablePropertyValues(configuration);
    }


}
