package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FilmControllerTest {

	@Autowired
	private Validator validator;
	private Film film;

	@BeforeEach
	public void setUp() {
		film = new Film();
	}

	@Test
	public void checkSuccessFilmValidation() {
		film.setId(1L);
		film.setName("Test");
		film.setDescription(RandomStringUtils.randomAlphabetic(200));
		film.setReleaseDate(LocalDate.parse("1895-12-28"));
		film.setDuration(120L);

		Set<ConstraintViolation<Film>> result = validator.validate(film);
		assertTrue(result.isEmpty(), "Ошибки по валидации быть не должно");
	}

	@Test
	public void whenNameIsNullThenValidationIsFailed() {
		film.setId(1L);
		film.setName("");
		film.setDescription("SuperTest");
		film.setReleaseDate(LocalDate.now().minusDays(1));
		film.setDuration(120L);

		List<ConstraintViolation<Film>> result = List.copyOf(validator.validate(film));
		assertEquals(1, result.size(), "Отсутствует ошибка по валидации поля");

		ConstraintViolation<Film> validationResult = result.getFirst();

		assertEquals("name", validationResult.getPropertyPath().toString());
		assertEquals("Название фильма не должно быть пустым", validationResult.getMessage());
	}

	@Test
	public void whenDescriptionLengthIs200ThenValidationIsFailed() {
		film.setId(1L);
		film.setName("Test");
		film.setDescription(RandomStringUtils.randomAlphabetic(201));
		film.setReleaseDate(LocalDate.now().minusDays(1));
		film.setDuration(120L);

		List<ConstraintViolation<Film>> result = List.copyOf(validator.validate(film));
		assertEquals(1, result.size(), "Отсутствует ошибка по валидации поля");

		ConstraintViolation<Film> validationResult = result.getFirst();

		assertEquals("description", validationResult.getPropertyPath().toString());
		assertEquals("Максимальное количество символов = 200", validationResult.getMessage());
	}

	@Test
	public void whenReleaseDateTooOldThenValidationIsFailed() {
		film.setId(1L);
		film.setName("Test");
		film.setDescription("SuperTest");
		film.setReleaseDate(LocalDate.parse("1895-12-27"));
		film.setDuration(120L);

		List<ConstraintViolation<Film>> result = List.copyOf(validator.validate(film));
		assertEquals(1, result.size(), "Отсутствует ошибка по валидации поля");

		ConstraintViolation<Film> validationResult = result.getFirst();

		assertEquals("releaseDate", validationResult.getPropertyPath().toString());
		assertEquals("Дата не должна быть раньше 1895-12-28", validationResult.getMessage());
	}

	@Test
	public void whenDurationIsNegativeThenValidationIsFailed() {
		film.setId(1L);
		film.setName("Test");
		film.setDescription("SuperTest");
		film.setReleaseDate(LocalDate.now().minusDays(1));
		film.setDuration(-1L);

		List<ConstraintViolation<Film>> result = List.copyOf(validator.validate(film));
		assertEquals(1, result.size(), "Отсутствует ошибка по валидации поля");

		ConstraintViolation<Film> validationResult = result.getFirst();

		assertEquals("duration", validationResult.getPropertyPath().toString());
		assertEquals("Продолжительность не может быть отрицательной", validationResult.getMessage());
	}

}
