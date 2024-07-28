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
    }
    
    likes {
        user_id int8 PK,FK
        film_id int8 PK,FK
    }
    
    ratings {
        int4 rating_id PK
        varchar name
    }

    film_ratings {
        int8 film_ratings_id PK
        int8 film_id FK
        int4 rating_id FK
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
        int8 user_id PK,FK
        int8 friend_id PK
        bool is_friend
    }
    
    genres||--||film_genres : is
    movies ||--o{ film_genres : contains
    users ||--|| likes : contains
    movies ||--o{ likes : contains
    movies ||--|| film_ratings : contains
    film_ratings ||--|| ratings : is
    users }o--o{ friends : contains
    
```