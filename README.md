# ScanAnnotations
    扫描编译后文件中的相关dubbo注解
    
    
拿到了项目，我们先对其进行编译。利用 javaasist 扫描项目，获取所有我们想要的接口，支持扫描 xml 文件 和 对应的注解


支持的类型：

- 1、扫描编译后的目录
- 2、扫描 jar / war 包
- 3、扫描 tar.gz 包 （其实也是先解压了扫描）



#### 相关的依赖：

```
<dependency>
        <groupId>org.javassist</groupId>
        <artifactId>javassist</artifactId>
        <version>3.24.1-GA</version>
    </dependency>
    <dependency>
        <groupId>com.google.guava</groupId>
        <artifactId>guava</artifactId>
        <version>15.0</version>
    </dependency>
    <!-- https://mvnrepository.com/artifact/dom4j/dom4j -->
    <dependency>
        <groupId>dom4j</groupId>
        <artifactId>dom4j</artifactId>
        <version>1.6.1</version>
    </dependency>
    <!-- https://mvnrepository.com/artifact/org.apache.commons/commons-compress -->
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-compress</artifactId>
        <version>1.20</version>
    </dependency>
```


#### 代码已上传仓库
https://github.com/daleyzou/ScanAnnotations


ps: 这是我为了获取指定项目中的所有dubbo生产者接口、所有dubbo消费者接口而编写的demo，所以我要扫描所有的注解和 xml 文件

