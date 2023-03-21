package io.github.mengfly.dynamicdb.resolver;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 动态数据源获取接口，通过实现该接口来动态加载数据源
 * <p>
 * 框架已经默认实现了从配置文件中获取数据源列表的功能
 *
 * @author Mengfly
 * @see PropertiesDataSourceResolver
 */
public interface DynamicDataSourceResolver {

    /**
     * 加载DataSource配置
     * <p>
     * 该接口返回数据源字典信息，其中Key为该数据源的标识符，Value为数据源
     * <p>
     * 动态数据源切换数据源的时候会根据该Key查询对应的DataSource
     *
     * @return 数据源配置
     * @throws Exception 加载数据源失败后触发此Exception
     * @see io.github.mengfly.dynamicdb.DynamicDataSourceHelper#setDataSource(String)
     */
    Map<Object, Object> loadDataSource() throws Exception;

    /**
     * 该方法会在 {@link #loadDataSource()} 后被调用，用于设置默认的数据源
     * <p>
     * 该数据源会默认赋予一个数据源Id
     * 所以在实现上面的 loadDataSource 方法时要注意不要和该数据源Id重复
     *
     * @return 默认的数据源
     * @throws Exception 加载数据源失败后触发此Exception
     */
    DataSource defaultDataSource() throws Exception;
}
