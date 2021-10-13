# Spring-dynamic-database-starter

> 动态数据库切换框架

## Usage

### 1. POM

```xml

<dependency>
    <groupId>io.github.mengfly</groupId>
    <artifactId>spring-dynamic-database-starter</artifactId>
    <version>1.0.0</version>
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

### 2. Usage On Code

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