# CRM REST API

Backend-приложение для управления продавцами и их транзакциями, разработанное на Java 21 и Spring Boot.

Проект реализует REST API с хранением данных в PostgreSQL, обработкой ошибок, аналитикой продаж и автоматизированными тестами.

## Технологии

- Java 21
- Spring Boot 4.1.1
- Spring Data JPA / Hibernate
- PostgreSQL 17
- Gradle
- Docker / Docker Compose
- Swagger / OpenAPI
- JUnit 5 / Mockito / AssertJ
- JaCoCo

## Функциональность

**Управление продавцами**
- Создание продавцов
- Получение списка продавцов
- Поиск продавца по ID
- Обновление информации
- Удаление через Soft Delete

**Управление транзакциями**
- Создание транзакций
- Получение и поиск транзакций
- Обновление и удаление транзакций
- Привязка транзакций к продавцам
- Поддержка типов оплаты CASH, CARD и TRANSFER

**Аналитика**
- Анализ за день, месяц, квартал или год

**Дополнительные возможности**
- Централизованная обработка ошибок
- REST API документация через Swagger
- Автоматизированные unit-тесты
- Запуск приложения и PostgreSQL через Docker Compose

## Запуск через Docker

### Требования

- Docker Desktop
- Java 21
- Git

### 1. Клонирование проекта

```bash
git clone <URL_РЕПОЗИТОРИЯ>
cd crm
```

### 2. Настройка окружения

Создайте файл `.env` на основе `.env.example`:

```bash
cp .env.example .env
```

Укажите собственный пароль PostgreSQL в `.env`:

```dotenv
POSTGRES_PASSWORD=your_password
```

### 3. Сборка приложения

```bash
./gradlew clean bootJar
```

### 4. Сборка Docker-образа

```bash
docker build -t crm-app:local .
```

### 5. Запуск контейнеров

```bash
docker compose up -d
```

Приложение будет доступно по адресу:

http://localhost:8080

Swagger UI:

http://localhost:8080/swagger-ui/index.html

### Остановка контейнеров

```bash
docker compose down
```

Данные PostgreSQL сохраняются в Docker volume.

## Примеры API

| Метод | Endpoint | Описание |
|---|---|---|
| GET | `/api/sellers` | Получить продавцов |
| GET | `/api/sellers/{id}` | Получить продавца по ID |
| GET | `/api/sellers/count` | Количество продавцов |
| POST | `/api/sellers` | Создать продавца |
| GET | `/api/transactions` | Получить транзакции |
| POST | `/api/transactions` | Создать транзакцию |
| GET | `/api/analytics/top-seller?period=MONTH` | Лучший продавец за месяц |

Полный перечень запросов доступен в Swagger UI.

## Тестирование

Для запуска тестов:

```bash
./gradlew test
```

Для формирования отчёта покрытия JaCoCo:

```bash
./gradlew jacocoTestReport
```

HTML-отчёт:

```text
build/reports/jacoco/test/html/index.html
```

На текущем этапе проекта успешно пройдено 26 тестов. Покрытие инструкций по JaCoCo составляет 64%.

## Структура проекта

```text
crm/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
├── gradle/
├── Dockerfile
├── compose.yaml
├── .env.example
├── .gitignore
├── build.gradle
├── settings.gradle
└── README.md
```

## Архитектура

Проект использует многослойную архитектуру:

- Controller — обработка HTTP-запросов
- Service — бизнес-логика и валидация
- Repository — доступ к базе данных
- Model — JPA-сущности
- DTO — структуры данных API
- Exception Handler — централизованная обработка ошибок

## Назначение проекта

Учебный backend-проект для практики Java, Spring Boot, REST API, PostgreSQL, тестирования и контейнеризации.
