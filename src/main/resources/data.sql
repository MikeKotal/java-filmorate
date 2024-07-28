INSERT INTO genres (name)
VALUES ('Комедия'), ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик');

INSERT INTO ratings (name)
VALUES ('G'), ('PG'), ('PG-13'), ('R'), ('NC-17');

INSERT INTO movies (name, description, release_date, duration, total_likes)
VALUES ('Фильм', 'Крутой фильм', '2024-07-01', 150, 1),
       ('Еще фильм', 'Круче предыдущего', '2024-07-02', 160, 0);

INSERT INTO users (email, login, name, birthday)
VALUES ('test@test.ru', 'test123', 'John', '1995-07-01'),
       ('email@email.com', 'qwerty123', 'Nick', '1995-07-02');

INSERT INTO film_genres (film_id, genre_id)
VALUES (1, 6), (2, 1);

INSERT INTO film_ratings (film_id, rating_id)
VALUES (1, 4);

INSERT INTO friends (user_id, friend_id)
VALUES (1, 2);

INSERT INTO likes (user_id, film_id)
VALUES (1, 1);