# Windows 10搭建GraalVM Native Image环境

## 安装GraalVM
下载`https://www.graalvm.org/downloads/`，解压安装

## 安装Visual Studio
下载`https://visualstudio.microsoft.com/zh-hans/downloads/`，在线安装

一般选择`使用 C++ 的桌面开发` -> `MSVC v143 - VS 2022 C++ x64/x86 生成工具`和`Windows 11 SDK`即可

步骤参考`https://blog.csdn.net/qq10940370/article/details/128064384`

## 设置环境变量
注意：版本号和目录，不同的机器上有所区别
```
JAVA_HOME=E:\Tool\Graalvm-JDK17-22.3.0

LIB=C:\Program Files (x86)\Windows Kits\10\Lib\10.0.22000.0\um\x64;C:\Program Files (x86)\Windows Kits\10\Lib\10.0.22000.0\ucrt\x64;E:\Tool\Microsoft Visual Studio\2022\Community\VC\Tools\MSVC\14.34.31933\lib\x64

INCLUDE=C:\Program Files (x86)\Windows Kits\10\Include\10.0.22000.0\ucrt;C:\Program Files (x86)\Windows Kits\10\Include\10.0.22000.0\um;C:\Program Files (x86)\Windows Kits\10\Include\10.0.22000.0\shared;E:\Tool\Microsoft Visual Studio\2022\Community\VC\Tools\MSVC\14.34.31933\include

PATH=E:\Tool\Microsoft Visual Studio\2022\Community\VC\Tools\MSVC\14.34.31933\bin\HostX64\x64
```

## 编写Maven
简单方式，必须以`spring-boot-starter-parent`引入方式
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.0.0</version>
</parent>
```
其它方式，参考`https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html#native-image.testing.with-native-build-tools.maven`

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

## 执行本地化打包
必须通过开始菜单的命令行
```
x64 Native Tools Command Prompt for VS 2022
```

运行打包命令
```
mvn -Pnative native:compile -DskipTests
```

或者直接运行根目录下`install-native.bat`，注意`bat`中GraalVM的路径