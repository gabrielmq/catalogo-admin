package com.fullcycle.catalogo.admin.application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UseCaseTest {

    @Test
    public void testCreateUseCase() {
        assertNotNull(new UseCase());
    }
}
