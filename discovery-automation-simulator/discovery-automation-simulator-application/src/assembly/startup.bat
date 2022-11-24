@echo on
@echo =============================================================
@echo $                                                           $
@echo $                Nepxion Discovery Simulator                $
@echo $                                                           $
@echo $                                                           $
@echo $                                                           $
@echo $  Nepxion Studio All Right Reserved                        $
@echo $  Copyright (C) 2017-2050                                  $
@echo $                                                           $
@echo =============================================================
@echo.
@echo off

@title Nepxion Discovery Simulator
@color 0a

call java -jar discovery-automation-simulator-1.1.0.jar --spring.config.location=./

pause