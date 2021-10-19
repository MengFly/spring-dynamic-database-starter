# Spring-dynamic-database-starter

> 动态数据库切换框架

## Usage

### 1. POM

```xml

<dependency>
    <groupId>io.github.mengfly</groupId>
    <artifactId>spring-dynamic-database-starter</artifactId>
    <version>1.1.1</version>
</dependency>
```

### 2. 配置文件

```yaml
spring:
  datasource:
    # 默认数据源配置
    drive-class-name: com.mysql.jdbc.Driver
    url: url
    username: username
    password: password
    # 数据源属性统一配置（示例）
    idleConnectionValidationSeconds: 100
    maxConnection: 100
    testWhileIdle: true
    testOnBorrow: true
    time-between-eviction-runs-millis: 600000
    min-evictable-idle-time-millis: 300000
    idleTimeout: 1000
    loginTimeout: 60000
    # 动态数据库列表
    targets:
      - id: ds0
        url: urlOfds0
        username: usernameOfds0
        password: passwordOfds0
        driver-class-name: driveOfds0
      - id: ds1
        url: urlOfds1
        username: usernameOfds1
        password: passwordOfds1
        driver-class-name: driveOfds1
```

### 3. Usage On Code

```java

DynamicDataSourceHelper.setDataSource("ds0");
// JPA
        System.out.println("jpa ds0 count: "+repository.count());
// Mybatis
        System.out.println("mybatis ds0 count: "+mapper.count());

// 切换数据源
        DynamicDataSourceHelper.setDataSource("ds1");
        System.out.println("jpa ds1 count: "+repository.count());
        System.out.println("mybatis ds1 count: "+mapper.count());

```

### 4. 程序运行时自动执行sql文件
> 数据库脚本自动执行功能支持两种不同的方式，和SpringBoot jdbc starter类似，一种通过配置文件方式配置
> 另外一种通过代码返回DynamicDataSourceInitializer的方式手动编写代码完成。  
> 示例如下：

1. 配置文件方式

```yaml
spring:
  datasource:
    # 开启执行脚本功能
    enable-script: true
    # 数据表脚本
    script-schema: classpath:schema.sql
    # 数据脚本
    script-data: classpath:data.sql
    # Other config ......
    targets:
      - id: ds1
        # other config .....
        schema: classpath:schemaOfds1.sql
        data: classpath:dataOfDs1.sql
```

2. 代码方式

```java

@Configuration
public class DynamicDbConfig {

    @Autowired
    private DynamicDataSource dataSource;

    @Bean
    public DynamicDataSourceInitializer initializer() {
        DynamicDataSourceInitializer initializer = new DynamicDataSourceInitializer();
        initializer.setDataSource(dataSource);

        // 主数据库
        ResourceDatabasePopulator indexPopulator = new ResourceDatabasePopulator();
        indexPopulator.addScripts(
                // Schema
                new ClassPathResource("db/data-index.sql"),
                // data
                new ClassPathResource("db/schema-index.sql")
        );
        initializer.setDatabaseInitializer(DynamicDataSourceHelper.DEFAULT_DATASOURCE_NAME, indexPopulator);

        // targets 数据库
        ResourceDatabasePopulator ptPopulator = new ResourceDatabasePopulator();
        ptPopulator.addScripts(
                // Schema
                new ClassPathResource("db/schema-pt.sql")
        );
        initializer.setDatabaseInitializer("ds1", ptPopulator);

        return initializer;
    }
}
```