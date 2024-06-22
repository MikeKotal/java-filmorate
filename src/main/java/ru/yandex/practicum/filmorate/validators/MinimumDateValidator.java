package ru.yandex.practicum.filmorate.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.annotations.MinimumDate;

import java.time.LocalDate;

public class MinimumDateValidator implements ConstraintValidator<MinimumDate, LocalDate> {
    private LocalDate minimumDate;

    @Override
    public void initialize(MinimumDate constraintAnnotation) {
        minimumDate = LocalDate.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate == null || !localDate.isBefore(minimumDate);
    }
}
