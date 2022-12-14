@echo on
@echo =============================================================
@echo $                                                           $
@echo $                Nepxion Discovery Inspector                $
@echo $                                                           $
@echo $                                                           $
@echo $                                                           $
@echo $  Nepxion Studio All Right Reserved                        $
@echo $  Copyright (C) 2017-2050                                  $
@echo $                                                           $
@echo =============================================================
@echo.
@echo off

@title Nepxion Discovery Inspector
@color 0a

call java -jar -Dnepxion.banner.shown.ansi.mode=true discovery-automation-inspector.jar --spring.config.location=./

pause