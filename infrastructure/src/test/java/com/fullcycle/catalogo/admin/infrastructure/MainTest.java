package com.fullcycle.catalogo.admin.infrastructure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MainTest {
    @Test
    public void testMain() {
        assertNotNull(new Main());
        Main.main(new String[]{});
    }
}
