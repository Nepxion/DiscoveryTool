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

call java -jar -Dnepxion.banner.shown.ansi.mode=true discovery-console.jar --spring.cloud.nacos.discovery.server-addr=localhost:8848 --spring.cloud.nacos.config.server-addr=localhost:8848

pause