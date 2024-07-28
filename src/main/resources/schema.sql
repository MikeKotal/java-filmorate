CREATE TABLE IF NOT EXISTS ratings (
    rating_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS movies (
    film_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(40) NOT NULL,
    description VARCHAR(200),
    release_date DATE NOT NULL,
    duration BIGINT NOT NULL,
    total_likes INTEGER DEFAULT 0,
    rating_id INTEGER REFERENCES ratings(rating_id)
);

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    login VARCHAR(40) NOT NULL UNIQUE,
    name VARCHAR(40),
    birthday DATE
);

CREATE TABLE IF NOT EXISTS friends (
    user_id BIGINT REFERENCES users(user_id),
    friend_id BIGINT REFERENCES users(user_id),
    PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS likes (
    user_id BIGINT REFERENCES users(user_id),
    film_id BIGINT REFERENCES movies(film_id),
    PRIMARY KEY (user_id, film_id)
);

CREATE TABLE IF NOT EXISTS genres (
    genre_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genres (
    film_id BIGINT REFERENCES movies(film_id),
    genre_id INTEGER REFERENCES genres(genre_id),
    PRIMARY KEY (film_id, genre_id)
);