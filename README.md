# FirstSpringProject

Если в событии содержится id клиента, оно заменяется на его полное имя, а также все события логируются разными способами 
в зависимости от их типа.

EventType.INFO - consoleEventLogger;
EventType.ERROR - combinedEventLogger, который включает в себя consoleEventLogger и fileEventLogger(вывод в файл log.txt);
null - вызывается логгер по умолчанию, в моём случае - это cacheFileEventLogger, который сначал добавляет события в cache, а потом уже 
выводит в файл.
