package io.github.mengfly.dynamicdb.initializer;

import io.github.mengfly.dynamicdb.DatasourceProperties;
import io.github.mengfly.dynamicdb.DynamicDataSource;
import io.github.mengfly.dynamicdb.DynamicDataSourceHelper;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.Collections;
import java.util.List;

/**
 * @author Mengfly
 */
public class PropertiesDataSourceInitializer implements InitializingBean, DisposableBean, EnvironmentAware {
    private Binder binder;
    private final DynamicDataSourceInitializer initializer = new DynamicDataSourceInitializer();

    private static final String SCRIPT_MAIN_DATA_SOURCE_SCHEMA = "spring.datasource.script-schema";
    private static final String SCRIPT_MAIN_DATA_SOURCE_DATA = "spring.datasource.script-data";
    private static final String TARGET_DATA_SOURCE = "spring.datasource.targets";
    private static final String SCRIPT_ENCODING_DATA_SOURCE = "spring.datasource.script-encoding";
    private final ResourceLoader resourceLoader = new DefaultResourceLoader();
    private String encoding;

    @Override
    public void destroy() throws Exception {
        initializer.destroy();
    }

    public void setDataSource(DynamicDataSource dataSource) {
        this.initializer.setDataSource(dataSource);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        loadDatabasePopulators();
        initializer.afterPropertiesSet();
    }

    private void loadDatabasePopulators() {
        // 加载主数据源信息
        String mainDsSchemaScript = binder.bind(SCRIPT_MAIN_DATA_SOURCE_SCHEMA, String.class).orElse(null);
        String mainDsDataScript = binder.bind(SCRIPT_MAIN_DATA_SOURCE_DATA, String.class).orElse(null);
        encoding = binder.bind(SCRIPT_ENCODING_DATA_SOURCE, String.class).orElse(null);
        bindDataSourcePopulator(DynamicDataSourceHelper.DEFAULT_DATASOURCE_NAME, mainDsSchemaScript, mainDsDataScript);

        Bindable<List<DatasourceProperties>> bindable = Bindable.listOf(DatasourceProperties.class);
        List<DatasourceProperties> datasourceProperties = binder.bind(TARGET_DATA_SOURCE, bindable).orElse(Collections.emptyList());

        for (DatasourceProperties datasourceProperty : datasourceProperties) {
            bindDataSourcePopulator(datasourceProperty.getId(), datasourceProperty.getSchema(), datasourceProperty.getData());
        }

    }

    private void bindDataSourcePopulator(String dataSourceId, String schemaScript, String dataScript) {
        if (schemaScript != null || dataScript != null) {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            if (encoding != null) {
                populator.setSqlScriptEncoding(encoding);
            }
            if (schemaScript != null) {
                populator.addScript(resourceLoader.getResource(schemaScript));
            }
            if (dataScript != null) {
                populator.addScript(resourceLoader.getResource(dataScript));
            }
            initializer.setDatabaseInitializer(dataSourceId, populator);
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.binder = Binder.get(environment);
    }
}
