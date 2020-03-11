# ScanAnnotations
    扫描编译后文件中的相关dubbo注解
    
拿到了项目，我们先对其进行编译。利用 javaasist 扫描项目，获取所有我们想要的接口，支持扫描 xml 文件 和 对应的注解

支持的类型：

1、扫描目录

2、扫描 jar / war 包

3、扫描 tar.gz 包 （其实也是先解压了扫描）
