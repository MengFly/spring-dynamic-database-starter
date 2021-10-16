package io.github.mengfly.dynamicdb.initializer;

import io.github.mengfly.dynamicdb.DynamicDataSource;
import io.github.mengfly.dynamicdb.DynamicDataSourceHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Mengfly
 */
public class DynamicDataSourceInitializer implements InitializingBean, DisposableBean {
    private static final Log logger = LogFactory.getLog(DynamicDataSourceInitializer.class);

    private DynamicDataSource dataSource;

    private final Map<String, DatabasePopulator> databaseInitializerMap = new LinkedHashMap<>();
    private final Map<String, DatabasePopulator> databaseCleanerMap = new LinkedHashMap<>();
    private boolean enabled = true;


    public void setDataSource(DynamicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setDatabaseInitializer(String id, DatabasePopulator initializer) {
        Assert.notNull(id, "Dynamic DataSource id must no null");
        databaseInitializerMap.put(id, initializer);
    }

    public void setDatabaseCleaner(String id, DatabasePopulator cleaner) {
        Assert.notNull(id, "Dynamic DataSource id must no null");
        databaseCleanerMap.put(id, cleaner);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void destroy() throws Exception {
        execute(this.databaseCleanerMap);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        execute(this.databaseInitializerMap);
    }

    private void execute(Map<String, DatabasePopulator> populatorMap) {
        Assert.state(this.dataSource != null, "Dynamic DataSource must be set");
        if (this.enabled) {
            for (String id : populatorMap.keySet()) {
                DynamicDataSourceHelper.setDataSource(id);
                DatabasePopulator populator = populatorMap.get(id);

                logger.info(String.format("Datasource %s start to execute script.", id));
                DatabasePopulatorUtils.execute(populator, this.dataSource);
                logger.info(String.format("Datasource %s execute script finish.", id));
            }
        }
    }
}
