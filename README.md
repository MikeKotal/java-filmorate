# java-filmorate
Template repository for Filmorate project.

# Модель базы данных
[Посмотреть детально диаграмму](https://dbdiagram.io/d/Filmorate-66939c0a9939893daedf2e4d)

```Для локального отображения схемы необходимо установить плагин Mermaid```

```mermaid
erDiagram
    movies {
        int8 film_id PK
        varchar name
        varchar description
        date release_date
        int8 duration
        int4 total_likes
        int4 genre_id
        int4 rating_id FK
    }
    
    likes {
        int8 like_id PK
        user_id int8 FK
        fiilm_id int8 FK
    }
    
    ratings {
        int4 rating_id PK
        varchar name
    }

    film_genres {
        int8 film_genre_id PK
        int8 film_id FK
        genre_id int4 FK
    }
    
    genres {
        int4 genre_id PK
        varchar name
    }
    users {
        int8 user_id PK
        varchar email
        varchar login
        varchar name
        date birthday
    }
    
    friends {
        int8 friendship_id PK
        int8 user_id FK
        int8 friend_id
        bool is_friend
    }
    
    genres||--||film_genres: is
    movies }o--o{ film_genres : contains
    users }o--o{ likes : contains
    movies }o--o{ likes : contains
    movies }o--|| ratings : contains
    users }o--o{ friends : contains
    
```

# Примеры запроса:
### 1. Выведем 10 популярных фильмов с отображением жанра, рейтинга и пользователей, кто поставил лайк
```
SELECT mv.name, mv.total_likes, g.name as genre_name, usr.login as user_login
FROM movies AS mv
LEFT JOIN film_genres AS fg ON mv.film_id=fg.film_id
LEFT JOIN genres AS g ON fg.genre_id=g.genre_id
LEFT JOIN ratings AS r ON mv.rating_id=r.rating_id
LEFT JOIN likes AS l ON mv.film_id=l.film_id
LEFT JOIN users AS usr ON l.user_id=usr.user_id
ORDER BY mv.total_likes DESC
LIMIT 10;
```

### 2. Выведем список друзей пользователя
```
SELECT usr.user_id, fr.user.id
FROM user AS usr
LEFT JOIN friends AS fr ON us.user_id=fr.user_id
```