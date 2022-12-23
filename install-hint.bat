@echo on
@echo =============================================================
@echo $                                                           $
@echo $                     Nepxion Discovery                     $
@echo $                                                           $
@echo $                                                           $
@echo $                                                           $
@echo $  Nepxion Studio All Right Reserved                        $
@echo $  Copyright (C) 2017-2050                                  $
@echo $                                                           $
@echo =============================================================
@echo.
@echo off

@title Nepxion Discovery
@color 0a

set JAVA_HOME=E:\Tool\Graalvm-JDK17-22.3.0
set PATH=%JAVA_HOME%\bin;%PATH%

@rem 执行之前，先把两个Application模块test/resources下的文件复制到main/resources下

@rem 执行Console的-agentlib命令
@set MODULE_PATH=discovery-automation-console
@set MODULE_JAR=discovery-automation-console.jar

@rem 执行Inspector的-agentlib命令
@rem @set MODULE_PATH=discovery-automation-inspector/discovery-automation-inspector-application
@rem @set MODULE_JAR=discovery-automation-inspector.jar

@rem 执行Simulator的-agentlib命令
@rem @set MODULE_PATH=discovery-automation-simulator/discovery-automation-simulator-application
@rem @set MODULE_JAR=discovery-automation-simulator.jar

@echo on
@echo After the Application started, please wait for a while, 5 seconds or more...
@echo Then press Ctrl + C to stop and generate relavent files to path src\main\resources\META-INF\native-image\
@echo.
@echo off

timeout 3 > NUL

call java -Dspring.aot.enabled=true -agentlib:native-image-agent=config-output-dir=%MODULE_PATH%/src/main/resources/META-INF/native-image -jar %MODULE_PATH%/target/%MODULE_JAR%

pause