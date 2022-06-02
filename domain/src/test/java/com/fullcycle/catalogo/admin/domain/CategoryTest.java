package com.fullcycle.catalogo.admin.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CategoryTest {

    @Test
    public void testNewCategory() {
        assertNotNull(new Category());
    }
}
