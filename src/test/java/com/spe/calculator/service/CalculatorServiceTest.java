package com.spe.calculator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculatorServiceTest {

    private CalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        calculatorService = new CalculatorService();
    }

    @Test
    void shouldAddTwoNumbers() {
        assertEquals(15.0, calculatorService.add(10, 5));
    }

    @Test
    void shouldSubtractTwoNumbers() {
        assertEquals(5.0, calculatorService.subtract(10, 5));
    }

    @Test
    void shouldMultiplyTwoNumbers() {
        assertEquals(50.0, calculatorService.multiply(10, 5));
    }

    @Test
    void shouldDivideTwoNumbers() {
        assertEquals(2.0, calculatorService.divide(10, 5));
    }

    @Test
    void shouldThrowExceptionWhenDividingByZero() {
        assertThrows(
                ArithmeticException.class,
                () -> calculatorService.divide(10, 0)
        );
    }
}