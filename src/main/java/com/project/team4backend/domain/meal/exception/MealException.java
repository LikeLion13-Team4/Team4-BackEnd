package com.project.team4backend.domain.meal.exception;

import com.project.team4backend.domain.meal.entity.Meal;
import com.project.team4backend.global.apiPayload.exception.CustomException;
import lombok.Getter;

@Getter
public class MealException extends CustomException {
    public MealException(MealErrorCode errorCode) {
        super(errorCode);
    }
}
