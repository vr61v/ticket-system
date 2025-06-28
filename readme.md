# Ticket system

## Содержание
- [Технологии](#технологии)
- [Установка](#установка)
- [Структура проекта](#структура-проекта)
- [Использование](#использование)
- [API](#api)

## Технологии

## Установка

## Структура проекта
```
   src/
   ├── main/
   │   ├── java/com/vr61v/
   │   │   ├── controllers/v1/
   │   │   │   ├── crud/                # Реализация crud контроллеров
   │   │   │   ├── custom/              # Реализация контроллеров с уникальной логикой
   │   │   │   └── CrudController       # Абстрактный класс crud контроллера
   │   │   ├── dtos/                    # Dto для общения с контроллером
   │   │   ├── embedded/                # Встраиваемые сущности
   │   │   ├── entities/                # Сущности jpa репозиториев
   │   │   ├── handlers/                # Перехватчики ошибок из контроллеров
   │   │   ├── mappers/                 # Интерфейсы для реализации мапперов сущностей
   │   │   ├── repositories/            # Интерфейсы для реализации jpa репозиториев
   │   │   ├── services/
   │   │   │   ├── crud/                # Реализация crud сервисов
   │   │   │   ├── custom/              # Реализация сервисов с уникальной логикой
   │   │   │   └── CrudService          # Абстрактный класс crud сервиса
   │   │   └── types/                   # Перечисления для работы с БД
   │   └── resources/                   # Конфигурационные файлы

   postgres/                            # Файлы для инициализации БД
   docker-compose.yml                   # Конфигурация запуска сервисов Docker
```

## Использование
> Для использования приложения необходимо либо собрать проект и запустить локально,
    либо собрать образ из Dockerfile и запустить в compose. Приложение требует для работы
    свободного порта 5432(postgres) и 8080(application). Для быстрого тестирования ендпоинтов
    можно импортировать spring-data.postman_collection.json, который в себе хранит все возможные
    базовые crud запросы (для большего понимания к коллекциям написано описание в самом postman)

## API

> [!NOTE]
> В случае отсутствия какого либо id в удаляемой коллекции будет возвращено сообщение,
> с указанием отсутствующих id.

> [!WARNING]
> Использование GET запроса для получения всех сущностей может привести к огромным задержкам
> по причине большой базы данных, перед использованием следует изучить размеры каждой из таблиц.

### Основные конечные точки

#### Самолеты (`/api/v1/aircrafts`)
- **GET /api/v1/aircrafts** - Получить список всех самолетов
- **GET /api/v1/aircrafts/{code}** - Получить самолет по коду
- **POST /api/v1/aircrafts/{code}** - Создать новый самолет
- **POST /api/v1/aircrafts** - Создать несколько самолетов (массив)
- **PUT /api/v1/aircrafts/{code}** - Обновить данные самолета
- **PUT /api/v1/aircrafts** - Обновить несколько самолетов (массив)
- **DELETE /api/v1/aircrafts/{code}** - Удалить самолет
- **DELETE /api/v1/aircrafts** - Удалить несколько самолетов (массив кодов)

#### Места в самолетах (`/api/v1/aircrafts/{code}/seats`)
- **GET /api/v1/aircrafts/{code}/seats** - Получить все места в самолете
- **GET /api/v1/aircrafts/{code}/seats/{seatNo}** - Получить конкретное место
- **POST /api/v1/aircrafts/{code}/seats/{seatNo}** - Создать новое место
- **POST /api/v1/aircrafts/{code}/seats** - Создать несколько мест (массив)
- **PUT /api/v1/aircrafts/{code}/seats/{seatNo}** - Обновить данные места
- **PUT /api/v1/aircrafts/{code}/seats** - Обновить несколько мест (массив)
- **DELETE /api/v1/aircrafts/{code}/seats/{seatNo}** - Удалить место
- **DELETE /api/v1/aircrafts/{code}/seats** - Удалить несколько мест (массив)

#### Аэропорты (`/api/v1/airports`)
- **GET /api/v1/airports** - Получить список всех аэропортов
- **GET /api/v1/airports/{code}** - Получить аэропорт по коду
- **POST /api/v1/airports/{code}** - Создать новый аэропорт
- **POST /api/v1/airports** - Создать несколько аэропортов (массив)
- **PUT /api/v1/airports/{code}** - Обновить данные аэропорта
- **PUT /api/v1/airports** - Обновить несколько аэропортов (массив)
- **DELETE /api/v1/airports/{code}** - Удалить аэропорт
- **DELETE /api/v1/airports** - Удалить несколько аэропортов (массив кодов)

#### Бронирования (`/api/v1/bookings`)
- **GET /api/v1/bookings** - Получить список всех бронирований
- **GET /api/v1/bookings/{ref}** - Получить бронирование по номеру
- **POST /api/v1/bookings/{ref}** - Создать новое бронирование
- **POST /api/v1/bookings** - Создать несколько бронирований (массив)
- **PUT /api/v1/bookings/{ref}** - Обновить данные бронирования
- **PUT /api/v1/bookings** - Обновить несколько бронирований (массив)
- **DELETE /api/v1/bookings/{ref}** - Удалить бронирование
- **DELETE /api/v1/bookings** - Удалить несколько бронирований (массив номеров)

#### Билеты (`/api/v1/tickets`)
- **GET /api/v1/tickets** - Получить список всех билетов
- **GET /api/v1/tickets/{number}** - Получить билет по номеру
- **POST /api/v1/tickets/{number}** - Создать новый билет
- **POST /api/v1/tickets** - Создать несколько билетов (массив)
- **PUT /api/v1/tickets/{number}** - Обновить данные билета
- **PUT /api/v1/tickets** - Обновить несколько билетов (массив)
- **DELETE /api/v1/tickets/{number}** - Удалить билет
- **DELETE /api/v1/tickets** - Удалить несколько билетов (массив номеров)

#### Рейсы (`/api/v1/flights`)
- **GET /api/v1/flights** - Получить список всех рейсов
- **GET /api/v1/flights/{id}** - Получить рейс по ID
- **POST /api/v1/flights/{id}** - Создать новый рейс
- **POST /api/v1/flights** - Создать несколько рейсов (массив)
- **PUT /api/v1/flights/{id}** - Обновить данные рейса
- **PUT /api/v1/flights** - Обновить несколько рейсов (массив)
- **DELETE /api/v1/flights/{id}** - Удалить рейс
- **DELETE /api/v1/flights** - Удалить несколько рейсов (массив ID)

#### Билеты на рейсы (`/api/v1/ticketflights`)
- **GET /api/v1/ticketflights** - Получить список всех билетов на рейсы
- **GET /api/v1/ticketflights/{ticketNo}/{flightId}** - Получить билет на рейс
- **POST /api/v1/ticketflights/{ticketNo}/{flightId}** - Создать билет на рейс
- **POST /api/v1/ticketflights** - Создать несколько билетов на рейсы (массив)
- **PUT /api/v1/ticketflights/{ticketNo}/{flightId}** - Обновить билет на рейс
- **PUT /api/v1/ticketflights** - Обновить несколько билетов на рейсы (массив)
- **DELETE /api/v1/ticketflights/{ticketNo}/{flightId}** - Удалить билет на рейс
- **DELETE /api/v1/ticketflights** - Удалить несколько билетов на рейсы (массив)

#### Посадочные талоны (`/api/v1/boardingpass`)
- **GET /api/v1/boardingpass** - Получить список всех посадочных талонов
- **GET /api/v1/boardingpass/{ticketNo}/{flightId}** - Получить посадочный талон
- **POST /api/v1/boardingpass/{ticketNo}/{flightId}** - Создать посадочный талон
- **POST /api/v1/boardingpass** - Создать несколько посадочных талонов (массив)
- **PUT /api/v1/boardingpass/{ticketNo}/{flightId}** - Обновить посадочный талон
- **PUT /api/v1/boardingpass** - Обновить несколько посадочных талонов (массив)
- **DELETE /api/v1/boardingpass/{ticketNo}/{flightId}** - Удалить посадочный талон
- **DELETE /api/v1/boardingpass** - Удалить несколько посадочных талонов (массив)