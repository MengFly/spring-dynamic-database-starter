package io.github.mengfly.dynamicdb;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * @author Mengfly
 */
public class DynamicDataSourceHelper {

    /**
     * 默认数据源Id
     */
    public static final String DEFAULT_DATASOURCE_NAME = "defaultDataSource";

    /**
     * 使用Alibaba的 TransmittableThreadLocal 解决数据源切换的父子线程之间数据传递的问题
     */
    private static final ThreadLocal<String> DATA_SOURCE_KEY = new TransmittableThreadLocal<>();

    public static void setDataSource(String dataSourceKey) {
        DATA_SOURCE_KEY.set(dataSourceKey);
    }

    public static void defaultDataSource() {
        setDataSource(DEFAULT_DATASOURCE_NAME);
    }

    public static String dataSourceKey() {
        String dataSourceKey = DATA_SOURCE_KEY.get();
        if (dataSourceKey == null) {
            return DEFAULT_DATASOURCE_NAME;
        }
        return dataSourceKey;
    }

    public static boolean containsDataSource(String datasource) {
        return DynamicDataSource.DATA_SOURCE_MAP.containsKey(datasource);
    }

    public static boolean containsDataSource(String datasource) {
        return DynamicDataSource.DATA_SOURCE_MAP.containsKey(datasource);
    }
}
