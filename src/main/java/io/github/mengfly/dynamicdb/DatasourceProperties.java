package io.github.mengfly.dynamicdb;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

/**
 * @author Mengfly
 */
public class DatasourceProperties {

    private static final Log logger = LogFactory.getLog(DynamicDataSource.class);
    private static final String DEFAULT_DATASOURCE_TYPE = "com.zaxxer.hikari.HikariDataSource";

    private String id;
    private String url;
    private String username;
    private String password;
    private String type;
    private String driverClassName;
    private String schema;
    private String data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }


    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @SuppressWarnings("unchecked")
    public DataSource newDataSource() throws ClassNotFoundException {
        Class<? extends DataSource> dataSourceClass;
        if (type == null) {
            logger.info(String.format("datasource %s use default (Hikari) dataSource", id));
            dataSourceClass = (Class<? extends DataSource>) Class.forName(DEFAULT_DATASOURCE_TYPE);
        } else {
            dataSourceClass = (Class<? extends DataSource>) Class.forName(type);
        }
        return DataSourceBuilder.create().type(dataSourceClass).driverClassName(driverClassName)
                .url(url).username(username).password(password).build();
    }
}
