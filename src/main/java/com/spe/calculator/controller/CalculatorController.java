package com.spe.calculator.controller;

import com.spe.calculator.service.CalculatorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class CalculatorController {

    private final CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    private void validateInputs(double a, double b) {
        if (!Double.isFinite(a) || !Double.isFinite(b)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Inputs must be finite numbers"
            );
        }
    }

    @GetMapping("/add")
    public Map<String, Object> add(
            @RequestParam double a,
            @RequestParam double b) {

        validateInputs(a, b);

        return Map.of(
                "operation", "add",
                "a", a,
                "b", b,
                "result", calculatorService.add(a, b)
        );
    }

    @GetMapping("/subtract")
    public Map<String, Object> subtract(
            @RequestParam double a,
            @RequestParam double b) {

        validateInputs(a, b);

        return Map.of(
                "operation", "subtract",
                "a", a,
                "b", b,
                "result", calculatorService.subtract(a, b)
        );
    }

    @GetMapping("/multiply")
    public Map<String, Object> multiply(
            @RequestParam double a,
            @RequestParam double b) {

        validateInputs(a, b);

        return Map.of(
                "operation", "multiply",
                "a", a,
                "b", b,
                "result", calculatorService.multiply(a, b)
        );
    }

    @GetMapping("/divide")
    public Map<String, Object> divide(
            @RequestParam double a,
            @RequestParam double b) {

        validateInputs(a, b);

        return Map.of(
                "operation", "divide",
                "a", a,
                "b", b,
                "result", calculatorService.divide(a, b)
        );
    }
}
