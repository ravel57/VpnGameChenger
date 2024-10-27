### Настройка для Wireguard

Для wireguard в конце группы intarface (на новой строке после MTU):
```table=off```
![изображение](https://github.com/user-attachments/assets/60ab1c7c-2d83-4352-bac2-7e60a5791768)
![изображение](https://github.com/user-attachments/assets/3af1cc5c-46e8-4d90-a9ad-b7e886fedfc3)


### Установка и запуск приложения

Установка приложения:
```WinSW-x64.exe install```


Запуск приложения:
```WinSW-x64.exe start```


Открыть приложение
```http://localhost:8666```


### В интерфейсе приложения:

gateway: `IP адрес из Wireguard без маски подсети (/ и цифры за ним)`

Название процесса: `название нужного процесса`
