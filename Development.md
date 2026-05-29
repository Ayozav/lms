## Архитектура для MVP
LMS представляет два основных сервиса:
- Сервис заметок “Перо”
- Основной сервис LMS

Первая реализация будет предполагать монолитную структуру

## Техническая реализация

1. Front-end: React + TypeScript + Material
2. Back-end: Java + Javalin 
3. Logto - SSO
4. Postgres - РСУБД
5. Nginx - web server/proxy

```mermaid
flowchart LR
    User[Клиент] -->|Запрос| OAuth2Proxy[OAuth2-Proxy :4180]

    OAuth2Proxy -->|Аутентификация| DexIdP[DexIdP]
    DexIdP -->|SQLite| SQLiteDB[(SQLite)]
    OAuth2Proxy -->|Кэш сессий| Redis[(Redis)]

    OAuth2Proxy -->|Прокси| Nginx[Nginx]

    Nginx -->|Статика| Frontend[Frontend]
    Nginx -->|/api/v1/*| Javalin[Javalin:4040]

    Javalin -->|GET| Postgres[(Postgres)]
    Javalin -->|CUD-операции| Kafka[Kafka]
    Kafka -->|Доставка команд| Javalin
    Javalin -->|Запись в БД| Postgres

    %% OpenObserve: продюсер (отправка данных) и консьюмер (получение конфигурации/алертов)
    Javalin -- "Логи \n (продюсер)" --> OpenObserve[OpenObserve]
    OpenObserve -- "Логи \n(консьюмер)" --> Javalin

```

## Полезные ссылки
- https://github.com/ayozav/lms
- https://www.figma.com/design/t6XW0X1KaQmsppxHwruGsb/LMS?node-id=0-1&t=xXMxyGN4yVOZLkNI-1
