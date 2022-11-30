@echo on
@echo =============================================================
@echo $                                                           $
@echo $            Nepxion Discovery Automation Console           $
@echo $                                                           $
@echo $                                                           $
@echo $                                                           $
@echo $  Nepxion Studio All Right Reserved                        $
@echo $  Copyright (C) 2017-2050                                  $
@echo $                                                           $
@echo =============================================================
@echo.
@echo off

@title Nepxion Discovery Automation Console
@color 0a

call java -jar -Dnepxion.banner.shown.ansi.mode=true discovery-automation-console-1.1.0.jar

pause