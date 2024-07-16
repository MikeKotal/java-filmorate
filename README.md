# java-filmorate
Template repository for Filmorate project.

# Модель базы данных
[Посмотреть детально диаграмму](https://dbdiagram.io/d/Filmorate-66939c0a9939893daedf2e4d)

```Для локального отображения схемы необходимо установить плагин Mermaid```

1. У таблицы ```movie``` связь в таблицей ```user``` по параметру ```user_id_like``` 
при добавлении лайка фильму пользователем сам пользователь линкуется с фильмом
2. У таблицы ```movie``` связь с таблицей ```genre``` по параметру ```genre_id```
при добавлении фильма, проставлется в таблицу идентификатор жанра, отдельная таблица жанров нужна для разрешения 
аномалий данных
3. У таблицы ```movie``` связь с таблицей ```rating``` по параметру ```rating_id```
при добавлении фильма, проставляется в таблицу идентификатор рейтинга
4. У таблицы ```user``` есть связь с таблицей ```friends```
при добавлении нового друга его идентификатор добавляется в таблицу друзей и в случае 
одобрения заявки на друзья - меняется признак ```is_friend```

```mermaid
erDiagram
    movie {
        int8 film_id PK
        varchar name
        varchar description
        date release_date
        int8 duration
        int8 user_id_like FK
        int4 total_likes
        int4 genre_id FK
        int4 rating_id
    }
    
    rating {
        int4 rating_id PK
        varchar name
    }
    
    genre {
        int4 genre_id PK
        varchar name
    }
    user {
        int8 user_id PK
        varchar email
        varchar login
        varchar name
        date birthday
    }
    
    friends {
        int8 user_id PK
        int8 friend_id
        bool is_friend
    }
    
    movie }o--o{ genre : contains
    movie }o--o{ user : contains
    movie }o--|| rating : contains
    user }o--o{ friends : contains
    
```

# Примеры запроса:
### 1. Выведем 10 популярных фильмов с отображением жанра, рейтинга и пользователей, кто поставил лайк
```
SELECT *
FROM movie AS mv
LEFT JOIN genre AS gn ON mv.genre_id=gn.genre_id
LEFT JOIN user AS us ON mv.user_id_like=us.user_id
LEFT JOIN rating AS rt ON mv.rating_id=rt.rating_id
ORDER BY mv.total_likes DESC
LIMIT 10;
```

### 2. Выведем список друзей пользователя
```
SELECT us.user_id, fr.user.id
FROM user AS us
LEFT JOIN friends AS fr ON us.user_id=fr.user_id
```