# Windows 10操作系统下基于Java 17 + Spring Boot 3 + GraalVM实现本地镜像化

## 前言
访问`https://github.com/Nepxion/DiscoveryTool/tree/automation-springboot-3.x.x`获取代码

`Nepxion`社区对相对简单的全链路自动化模拟流程测试和全链路自动化流量侦测测试模块尝试进行本地化，的确启动速度提升`10`倍甚至更多，内存占用少了`1/2 ~ 2/3`甚至更多，网络吞吐量也有不少提升。最终成果通过`Windows 10`的可执行文件`EXE`呈现，从下面图片可以看到，启动控制台，只耗费`0.2`秒

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
以`discovery-automation-console`模块为例，进入`discovery-automation-console`目录

### 执行`Hint`命令
> 注意：`discovery-automation-console`模块已经创建好`native-image`的反射文件，该步骤不需要运行，本文只介绍其用法

本地化之前需要把反射、`JNI`等类通过`-agentlib`命令生成这些类的`Json`形式的信息，创建到`src\main\resources\META-INF\native-image\`目录下

① 在`discovery-automation-console`目录下，运行如下命令
```
mvn -Pnative clean spring-boot:run
```
或者直接运行根目录下`install-hint.bat`，注意`bat`中`GraalVM`的路径

② 应用启动后，等待一段时间（`5`秒或者`10`秒，或者更长），执行`CTRL + C`快捷键结束应用进程后，自动创建`Hint`相关文件

`Hint`命令执行过程中，需要注意：
- `Hint`相关文件需要在`Spring Boot 3.0`应用的运行期创建相关文件，所以需要手工通过`CTRL + C`方式结束应用进程后才能创建那些文件，`GraalVM`官方插件有提供`config-write-period-secs`和`config-write-initial-delay-secs`的超时结束线程参数，请自行研究
- 如果应用中包含的包，是`Java 8`编译出来的，里面还有一些需要通过`Json`序列化和反序列化的实体类，需要对该包对应的源码用`Java 17`再编译一次，然后再执行`Hint`命令

### 执行`Native`命令
① 在`discovery-automation-console`目录下，运行如下命令
```
mvn -Pnative native:compile -DskipTests
```
或者直接运行根目录下`install-native.bat`，注意`bat`中`GraalVM`的路径

② 等待`1 ~ 2`分钟（取决于计算机的配置优劣），在`target`目录下，会创建`discovery-automation-console.exe`，可以直接运行，或者也可以通过`startup.exe.bat`来运行

### 编写本地化插件
访问`https://github.com/Nepxion/DiscoveryTool/blob/automation-springboot-3.x.x/pom.xml`，参考`profile`为`<id>native</id>`部分

## 参考资料
① `Spring Boot`官方文档 `https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html#native-image.developing-your-first-application.buildpacks.maven`

② `GraalVM`官方文档 `https://www.graalvm.org/22.3/reference-manual/native-image/metadata/AutomaticMetadataCollection/#tracing-agent`

③ `GraalVM`官方示例 `https://github.com/graalvm/native-build-tools/blob/master/samples/java-application-with-reflection/pom.xml`