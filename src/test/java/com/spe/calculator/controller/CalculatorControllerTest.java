package com.spe.calculator.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAddTwoNumbers() throws Exception {
        mockMvc.perform(get("/api/v1/add")
                        .param("a", "10")
                        .param("b", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.operation").value("add"))
                .andExpect(jsonPath("$.a").value(10.0))
                .andExpect(jsonPath("$.b").value(5.0))
                // TEMPORARY: Deliberately incorrect to test Jenkins failure notifications.
                .andExpect(jsonPath("$.result").value(15.0));
    }

    @Test
    void shouldSubtractTwoNumbers() throws Exception {
        mockMvc.perform(get("/api/v1/subtract")
                        .param("a", "10")
                        .param("b", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(5.0));
    }

    @Test
    void shouldMultiplyTwoNumbers() throws Exception {
        mockMvc.perform(get("/api/v1/multiply")
                        .param("a", "10")
                        .param("b", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(50.0));
    }

    @Test
    void shouldDivideTwoNumbers() throws Exception {
        mockMvc.perform(get("/api/v1/divide")
                        .param("a", "10")
                        .param("b", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(2.0));
    }

    @Test
    void shouldReturnBadRequestWhenDividingByZero() throws Exception {
        mockMvc.perform(get("/api/v1/divide")
                        .param("a", "10")
                        .param("b", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Division by zero is not allowed"));
    }

    @Test
    void shouldReturnBadRequestWhenParameterIsMissing() throws Exception {
        mockMvc.perform(get("/api/v1/add")
                        .param("a", "10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldReturnBadRequestWhenParameterIsInvalid() throws Exception {
        mockMvc.perform(get("/api/v1/add")
                        .param("a", "hello")
                        .param("b", "5"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldRejectNaNInput() throws Exception {
        mockMvc.perform(get("/api/v1/add")
                        .param("a", "NaN")
                        .param("b", "2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectPositiveInfinityInput() throws Exception {
        mockMvc.perform(get("/api/v1/add")
                        .param("a", "Infinity")
                        .param("b", "2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectNegativeInfinityInput() throws Exception {
        mockMvc.perform(get("/api/v1/add")
                        .param("a", "-Infinity")
                        .param("b", "2"))
                .andExpect(status().isBadRequest());
    }
}
