# Windows 10操作系统下基于Java 17 + Spring Boot 3 + GraalVM实现本地镜像化

## 前言
访问`https://github.com/Nepxion/DiscoveryTool/tree/automation-springboot-3.x.x`获取代码

`Nepxion`社区对相对简单的全链路自动化模拟流程测试和全链路自动化流量侦测测试模块尝试进行本地化，的确启动速度提升`10`倍甚至更多，内存占用少了`1/2 ~ 2/3`甚至更多，网络吞吐量也有不少提升。最终成果通过`Windows 10`的可执行文件`EXE`呈现，从下面图片可以看到，启动控制台，只耗费`0.24`秒

![](http://nepxion.gitee.io/discovery/docs/discovery-doc/NativeImage.jpg)

`Nepxion`社区分别对`Java 8`、`Java1 7`和`Spring Boot 2`、`Spring Boot 3`组合场景下，对Spring Cloud Gateway、2个A服务、2个B服务做全链路自动化蓝绿灰度发布模拟流程测试，得出如下结论：
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

## 执行本地化

本地化分为非`spring-boot-starter-parent`和`spring-boot-starter-parent`两种模式，源代码中实现的是非`spring-boot-starter-parent`模式

### 非`spring-boot-starter-parent`模式

① 编写`pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <artifactId>discovery-automation-console</artifactId>
    <name>Nepxion Discovery Automation Console</name>
    <packaging>jar</packaging>
    <modelVersion>4.0.0</modelVersion>
    <description>Nepxion Discovery Automation is a tool for Spring Cloud with blue green, gray inspection and simulation testing</description>
    <url>http://www.nepxion.com</url>

    <parent>
        <groupId>com.nepxion</groupId>
        <artifactId>discovery-automation</artifactId>
        <version>2.0.0</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>${project.groupId}</groupId>
            <artifactId>discovery-automation-inspector-starter-console</artifactId>
        </dependency>

        <dependency>
            <groupId>${project.groupId}</groupId>
            <artifactId>discovery-automation-simulator-starter-console</artifactId>
        </dependency>

        <dependency>
            <groupId>${project.groupId}</groupId>
            <artifactId>discovery-automation-concurrent-starter-caffeine</artifactId>
        </dependency>

        <!-- <dependency>
            <groupId>${project.groupId}</groupId>
            <artifactId>discovery-automation-concurrent-starter-redisson</artifactId>
        </dependency> -->
    </dependencies>

    <build>
        <finalName>${project.artifactId}</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>3.0.0</version>
                <configuration>
                    <mainClass>com.nepxion.discovery.automation.console.AutomationConsole</mainClass>
                    <layout>JAR</layout>
                    <layers>
                        <enabled>true</enabled>
                    </layers>
                </configuration>
                <executions>
                    <execution>
                        <id>process-package</id>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                        <configuration>
                            <attach>false</attach>
                        </configuration>
                    </execution>
                    <execution>
                        <id>process-aot</id>
                        <goals>
                            <goal>process-aot</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

            <plugin>
                <groupId>org.graalvm.buildtools</groupId>
                <artifactId>native-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <goals>
                            <goal>add-reachability-metadata</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

② 执行本地化打包

运行打包命令
```
mvn -Pnative native:compile -DskipTests
```
或者直接运行根目录下`install-native.bat`，注意`bat`中`GraalVM`的路径

> 请注意，由于事先已经生成了反射、`JNI`等类的`Json`信息，可以跳过`执行反射代理`步骤，具体可参考`spring-boot-starter-parent`模式下的相关介绍

### `spring-boot-starter-parent`模式

① 编写`pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <groupId>com.nepxion</groupId>
    <artifactId>discovery-automation-console</artifactId>
    <name>Nepxion Discovery Automation Console</name>
    <packaging>jar</packaging>
    <modelVersion>4.0.0</modelVersion>
    <version>2.0.0</version>
    <description>Nepxion Discovery Automation is a tool for Spring Cloud with blue green, gray inspection and simulation testing</description>
    <url>http://www.nepxion.com</url>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.0.0</version>
    </parent>

    <properties>
        <discovery.automation.version>2.0.0</discovery.automation.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>com.nepxion</groupId>
            <artifactId>discovery-automation-inspector-starter-console</artifactId>
            <version>${discovery.automation.version}</version>
        </dependency>

        <dependency>
            <groupId>com.nepxion</groupId>
            <artifactId>discovery-automation-simulator-starter-console</artifactId>
            <version>${discovery.automation.version}</version>
        </dependency>

        <dependency>
            <groupId>com.nepxion</groupId>
            <artifactId>discovery-automation-concurrent-starter-caffeine</artifactId>
            <version>${discovery.automation.version}</version>
        </dependency>

        <!-- <dependency>
            <groupId>com.nepxion</groupId>
            <artifactId>discovery-automation-concurrent-starter-redisson</artifactId>
            <version>${discovery.automation.version}</version>
        </dependency> -->
    </dependencies>

    <build>
        <finalName>${project.artifactId}</finalName>
        <plugins>
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
        </plugins>
    </build>
</project>
```

② 执行反射代理

由于`Java17`的反射机制进行了变更，需要实现把反射、`JNI`等类通过`-agentlib`命令生成这些类的`Json`形式的信息，创建到`src\main\resources\META-INF\native-image\`目录下

在根目录，运行反射命令
```
java -Dspring.aot.enabled=true -agentlib:native-image-agent=config-output-dir=discovery-automation-console/src/main/resources/META-INF/native-image -jar discovery-automation-console/target/discovery-automation-console.jar
```
或者直接运行根目录下`install-agent.bat`，注意`bat`中`GraalVM`的路径

如果应用中包含的包，是`Java 8`编译出来的，里面还有一些需要通过`Json`序列化和反序列化的实体类，需要对该包对应的源码用`Java 17`再编译一次，然后在执行反射代理的步骤

> 注意：`discovery-automation-console`模块已经创建好`native-image`的反射文件，该步骤不需要运行，本文只介绍其用法

③ 执行本地化打包

必须通过`开始菜单`中的如下命令行，执行本地化打包操作。这跟非`spring-boot-starter-parent`模式下的执行步骤有所区别
```
x64 Native Tools Command Prompt for VS 2022
```

运行打包命令
```
mvn -Pnative native:compile -DskipTests
```
或者直接运行根目录下`install-native.bat`，注意`bat`中`GraalVM`的路径

## 参考资料
① `Spring Boot`官方文档 `https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html#native-image.developing-your-first-application.buildpacks.maven`