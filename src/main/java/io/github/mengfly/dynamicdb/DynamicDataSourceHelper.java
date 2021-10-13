package io.github.mengfly.dynamicdb;

/**
 * @author Mengfly
 */
public class DynamicDataSourceHelper {

    /**
     * 默认数据源Id
     */
    public static final String DEFAULT_DATASOURCE_NAME = "defaultDataSource";

    private static final ThreadLocal<String> DATA_SOURCE_KEY = ThreadLocal.withInitial(() -> DEFAULT_DATASOURCE_NAME);

    public static void setDataSource(String dataSourceKey) {
        DATA_SOURCE_KEY.set(dataSourceKey);
    }

    public static void defaultDataSource() {
        setDataSource(DEFAULT_DATASOURCE_NAME);
    }

    public static String dataSourceKey() {
        return DATA_SOURCE_KEY.get();
    }
}
