package ru.yandex.practicum.filmorate.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.Past;
import ru.yandex.practicum.filmorate.validators.MinimumDateValidator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinimumDateValidator.class)
@Past
public @interface MinimumDate {
    String message() default "Дата не должна быть раньше {value}";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};

    String value() default "1895-12-28";
}
