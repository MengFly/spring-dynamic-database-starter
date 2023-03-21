package io.github.mengfly.dynamicdb;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * @author Mengfly
 */
public class DynamicDataSourceHelper {
    

    /**
     * 使用Alibaba的 TransmittableThreadLocal 解决数据源切换的父子线程之间数据传递的问题
     */
    private static final ThreadLocal<String> DATA_SOURCE_KEY = new TransmittableThreadLocal<>();

    public static void setDataSource(String dataSourceKey) {
        DATA_SOURCE_KEY.set(dataSourceKey);
    }

    public static void defaultDataSource() {
        DATA_SOURCE_KEY.remove();
    }

    public static String dataSourceKey() {
        return DATA_SOURCE_KEY.get();
    }

    public static boolean containsDataSource(String datasource) {
        return DynamicDataSource.DATA_SOURCE_MAP.containsKey(datasource);
    }
}
