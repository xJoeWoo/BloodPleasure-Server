## 配置
配置文件位于 `src/main/resource/application.yml` 

* 数据库
```yaml
spring:
  datasource:
    url: {数据库url}
    username: {数据库用户名}
    password: {数据库密码}
```
* 监听请求
```yaml
server:
  port: {端口}
  address: {监听地址}
```


## 生成

`./gradlew bootJar`

## 运行

`java -jar build/libs/bloodpleasure-{版本号}.jar`