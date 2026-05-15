# Практика 6: Maven, JDBC и Hibernate ORM

**Дисциплина:** Современные технологии программирования  
**Семестр:** 2  
**Дата:** 10.05.2026  

---

## Цели практики

1. Освоить структуру Maven-проекта и работу с зависимостями
2. Научиться выполнять CRUD операции через JDBC
3. Понять основы ORM и работу с Hibernate
4. Сравнить подходы JDBC vs Hibernate

---

## Структура репозитория

```
ModernTechnologiesPractice6/
├── part1/          # Maven и зависимости
├── part2/          # JDBC
├── part3/          # Hibernate ORM
├── answers/        # Контрольные вопросы
└── teacher/        # Материалы для преподавателя
```

---

## Таблица отслеживания прогресса

| № | ФИО | Группа | p1_1 | p1_2 | p2_1 | p2_2 | p3_1 | p3_2 | Вопросы |
|---|-----|--------|------|------|------|------|------|------|---------|
| 1 |     |        | ☐    | ☐    | ☐    | ☐    | ☐    | ☐    | ☐       |
| 2 |     |        | ☐    | ☐    | ☐    | ☐    | ☐    | ☐    | ☐       |
| 3 |     |        | ☐    | ☐    | ☐    | ☐    | ☐    | ☐    | ☐       |
| 4 |     |        | ☐    | ☐    | ☐    | ☐    | ☐    | ☐    | ☐       |
| 5 |     |        | ☐    | ☐    | ☐    | ☐    | ☐    | ☐    | ☐       |
| 6 |     |        | ☐    | ☐    | ☐    | ☐    | ☐    | ☐    | ☐       |
| 7 |     |        | ☐    | ☐    | ☐    | ☐    | ☐    | ☐    | ☐       |
| 8 |     |        | ☐    | ☐    | ☐    | ☐    | ☐    | ☐    | ☐       |
| 9 |     |        | ☐    | ☐    | ☐    | ☐    | ☐    | ☐    | ☐       |
| 10|     |        | ☐    | ☐    | ☐    | ☐    | ☐    | ☐    | ☐       |
| 11|     |        | ☐    | ☐    | ☐    | ☐    | ☐    | ☐    | ☐       |
| 12|     |        | ☐    | ☐    | ☐    | ☐    | ☐    | ☐    | ☐       |
| 13|     |        | ☐    | ☐    | ☐    | ☐    | ☐    | ☐    | ☐       |
| 14|     |        | ☐    | ☐    | ☐    | ☐    | ☐    | ☐    | ☐       |
| 15|     |        | ☐    | ☐    | ☐    | ☐    | ☐    | ☐    | ☐       |

---

## Часть 1: Maven

### Задание p1_1: Настройка Maven проекта

**Файлы:** `part1/p1_1/pom.xml`, `part1/p1_1/src/main/java/com/movies/Main.java`

**Описание:** Настройте Maven проект для работы с базой данных H2 и Hibernate.

**TODO:**
1. В `pom.xml` добавьте зависимости:
   - H2 Database: `com.h2database:h2:2.2.224`
   - Hibernate Core: `org.hibernate.orm:hibernate-core:6.4.0.Final`
2. Установите `maven.compiler.source` и `target` в 21
3. В `Main.java` выведите "Movie App Started"

**Команды для проверки:**
```bash
cd part1/p1_1
mvn compile
mvn package
mvn clean
mvn dependency:list
```

---

### Задание p1_2: Анализ зависимостей

**Файл:** `part1/p1_2/dependency_analysis.md`

**Описание:** Проанализируйте дерево зависимостей Maven и ответьте на вопросы.

**Вопросы:**
1. Сколько прямых (direct) зависимостей имеет ваш проект?
2. Сколько транзитивных зависимостей добавляет Hibernate Core?
3. Перечислите 3 транзитивных зависимости, которые подтягивает Hibernate.

**Полезная команда:**
```bash
mvn dependency:tree
```

---

## Часть 2: JDBC

### Задание p2_1: CRUD операции с JDBC

**Файл:** `part2/p2_1/MovieJDBC.java`

**Описание:** Реализуйте CRUD операции для таблицы movies используя JDBC.

**TODO:**
1. Реализуйте `insertMovies(Connection conn)` - вставьте 4 записи о фильмах с помощью PreparedStatement
2. Реализуйте `findByTitle(Connection conn, String title)` - поиск по названию с использованием LIKE

**Примечание:** Методы `dropAndCreateTable()`, `updateMovie()`, `deleteMovie()`, `printAllMovies()`, `findByYear()`, `findByGenre()` и `main()` уже реализованы.

**Ожидаемый результат:**
```
=== Все фильмы ===
ID: 1, Название: Матрица, Жанр: sci-fi, Год: 1999
...

=== Поиск по названию 'Матрица' ===
ID: 1, Название: Матрица, Жанр: sci-fi, Год: 1999
```

---

### Задание p2_2: Вопросы по JDBC

**Файл:** `part2/p2_2/questions.md`

**Вопросы:**
1. Почему используется LIKE с % в `findByTitle`?
2. Что такое PreparedStatement и почему он безопаснее Statement?

---

## Часть 3: Hibernate ORM

### Задание p3_1: CRUD с Hibernate

**Файл:** `part3/p3_1/MovieHibernate.java`

**Описание:** Реализуйте CRUD операции используя Hibernate ORM.

**TODO:**
1. Реализуйте `saveMovie(Session session, Movie movie)` - сохранение новой сущности
2. Реализуйте `findMoviesByGenre(Session session, String genre)` - поиск по жанру через HQL

**Примечание:** Класс сущности `Movie`, инициализация SessionFactory, методы `updateMovie()`, `deleteMovie()` и `main()` уже реализованы.

**Ожидаемый результат:**
```
=== Сохранение фильмов ===
Фильмы сохранены

=== Поиск по жанру 'sci-fi' ===
Movie{id=1, title='Матрица', genre='sci-fi', year=1999}
...
```

---

### Задание p3_2: Вопросы по Hibernate

**Файл:** `part3/p3_2/questions.md`

**Вопросы:**
1. В чем разница между JDBC и Hibernate?
2. Что такое HQL и чем он отличается от SQL?
3. Какие основные аннотации Hibernate используются на сущности Movie?

---

## Контрольные вопросы

Ответы на следующие вопросы необходимо записать в файл `answers/questions.md`:

1. Что такое Maven и зачем он нужен?
2. Чем отличается прямая зависимость от транзитивной?
3. Почему PreparedStatement безопаснее Statement?
4. Что такое ORM и какую проблему он решает?
5. Чем HQL отличается от SQL?

---

## Как работать с репозиторием

1. **Клонируйте репозиторий:**
   ```bash
   git clone <url-репозитория>
   cd ModernTechnologiesPractice6
   ```

2. **Убедитесь, что установлен Maven:**
   ```bash
   mvn -version
   ```

3. **Выполняйте задания по порядку:**
   - Начните с part1/p1_1 (настройка pom.xml)
   - Компилируйте и тестируйте каждый шаг

4. **Для JDBC и Hibernate используйте H2:**
   - База данных создается автоматически в памяти
   - Не требует отдельной установки

---

## Полезные команды Maven

```bash
# Компиляция
mvn compile

# Запуск
mvn exec:java -Dexec.mainClass="com.movies.Main"

# Сборка JAR
mvn package

# Очистка
mvn clean

# Дерево зависимостей
mvn dependency:tree

# Список зависимостей
mvn dependency:list
```

---

## Требования к среде

- **JDK:** OpenJDK 21 или выше
- **Maven:** 3.8+
- **IDE:** IntelliJ IDEA Community Edition
- **База данных:** H2 (встроенная, через Maven)
