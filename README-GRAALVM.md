# Windows 10操作系统下基于Java 17 + Spring Boot 3 + GraalVM实现本地镜像化

## 前言
访问`https://github.com/Nepxion/DiscoveryTool/tree/automation-springboot-3.x.x`获取代码

`Nepxion`社区对相对简单的自动化测试模块尝试进行本地化，的确启动速度提升`10`倍甚至更多，内存占用少了`1/2 ~ 2/3`甚至更多，网络吞吐量也有不少提升。最终成果通过`Windows 10`的可执行文件`EXE`呈现，从下面图片可以看到，启动控制台，只耗费`0.24`秒

![](http://nepxion.gitee.io/discovery/docs/discovery-doc/NativeImage.jpg)

`Nepxion`社区分别对`Java 8`、`Java1 7`和`Spring Boot 2`、`Spring Boot 3`组合场景下，对Spring Cloud Gateway、2个A服务、2个B服务做自动化模拟蓝绿灰度发布测试，得出如下结论：
- 全套非本地化，`Spring Boot 2` + `Java 8`，自动化测试耗时`180-200`秒
- 全套非本地化，`Spring Boot 2` + `Java 17`，自动化测试耗时`109-115`秒
- 全套非本地化，`Spring Boot 3` + `Java 17`，自动化测试耗时`93-97`秒
- 自动化测试端本地化，网关和服务非本地化，`Spring Boot 3` + `Java 17`，自动化测试耗时`80-83`秒
- 全套本地化，未测试，原因是网关和服务所依赖的所有第三方包都需要支持`Spring Boot 3`，目前业界尚未达到这个阶段

下面阐述一下，Windows 10操作系统下基于`Java 17` + `Spring Boot 3` + `GraalVM`实现服务本地镜像化（Native Image）的简单步骤

## 安装GraalVM
下载`https://www.graalvm.org/downloads/`，解压安装

## 安装Visual Studio
下载`https://visualstudio.microsoft.com/zh-hans/downloads/`，在线安装

一般选择`使用 C++ 的桌面开发` -> `MSVC v143 - VS 2022 C++ x64/x86 生成工具`和`Windows 11 SDK`即可

## 设置环境变量
```
JAVA_HOME=E:\Tool\Graalvm-JDK17-22.3.0

LIB=C:\Program Files (x86)\Windows Kits\10\Lib\10.0.22000.0\um\x64;C:\Program Files (x86)\Windows Kits\10\Lib\10.0.22000.0\ucrt\x64;E:\Tool\Microsoft Visual Studio\2022\Community\VC\Tools\MSVC\14.34.31933\lib\x64

INCLUDE=C:\Program Files (x86)\Windows Kits\10\Include\10.0.22000.0\ucrt;C:\Program Files (x86)\Windows Kits\10\Include\10.0.22000.0\um;C:\Program Files (x86)\Windows Kits\10\Include\10.0.22000.0\shared;E:\Tool\Microsoft Visual Studio\2022\Community\VC\Tools\MSVC\14.34.31933\include

PATH=E:\Tool\Microsoft Visual Studio\2022\Community\VC\Tools\MSVC\14.34.31933\bin\HostX64\x64
```

> 注意：版本号和目录，不同的机器上有所区别，请自行更改

## 编写Maven
简单方式，必须以`spring-boot-starter-parent`引入方式
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.0.0</version>
</parent>
```
其它方式，参考Spring官方文档 `https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html#native-image.testing.with-native-build-tools.maven`

打包插件引入
```
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <mainClass>com.nepxion.discovery.automation.console.AutomationConsole</mainClass>
        <layout>JAR</layout>
        <layers>
            <enabled>true</enabled>
        </layers>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>repackage</goal>
            </goals>
            <configuration>
                <attach>false</attach>
            </configuration>
        </execution>
    </executions>
</plugin>

<plugin>
    <groupId>org.graalvm.buildtools</groupId>
    <artifactId>native-maven-plugin</artifactId>
</plugin>
```

## 执行反射代理
由于`Java17`的反射机制进行了变更，需要实现把反射、JNI等类通过`-agentlib`命令生成这些类的`Json`形式的信息，创建到`src\main\resources\META-INF\native-image\`目录下

运行反射命令
```
java -Dspring.aot.enabled=true -agentlib:native-image-agent=config-output-dir=discovery-automation-console/src/main/resources/META-INF/native-image -jar discovery-automation-console/target/discovery-automation-console.jar
```

或者直接运行根目录下`install-agent.bat`，注意`bat`中`GraalVM`的路径

如果应用中包含的包，是`Java 8`编译出来的，里面还有一些需要通过`Json`序列化和反序列化的实体类，需要对该包对应的源码用`Java 17`再编译一次，然后在执行反射代理的步骤

> 注意：discovery-automation-console模块已经创建好native-image的反射文件，该步骤不需要运行

## 执行本地化打包
必须通过`开始菜单`中的如下命令行，执行本地化打包操作
```
x64 Native Tools Command Prompt for VS 2022
```

运行打包命令
```
mvn -Pnative native:compile -DskipTests
```

或者直接运行根目录下`install-native.bat`，注意`bat`中`GraalVM`的路径

## 总结
`Java 17` + `Spring Boot 3` + `GraalVM`实现服务本地镜像化，执行过程遇到的坑不少。例如
- 由于`Spring Boot 3`默认采用`Logback`日志，如果引入`Apache Common Logging`，编译无法通过
- `@PostStruct`注解下的方法不能为`private`