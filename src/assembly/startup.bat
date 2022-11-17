@echo on
@echo =============================================================
@echo $                                                           $
@echo $                 Nepxion Discovery Console                 $
@echo $                                                           $
@echo $                                                           $
@echo $                                                           $
@echo $  Nepxion Studio All Right Reserved                        $
@echo $  Copyright (C) 2017-2050                                  $
@echo $                                                           $
@echo =============================================================
@echo.
@echo off

@title Nepxion Discovery Console
@color 0a

call java -jar discovery-console-1.1.0.jar --spring.cloud.nacos.discovery.server-addr=localhost:8848 --spring.cloud.nacos.config.server-addr=localhost:8848

pause