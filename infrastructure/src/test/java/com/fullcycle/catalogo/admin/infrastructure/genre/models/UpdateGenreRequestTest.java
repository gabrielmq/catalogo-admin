package com.fullcycle.catalogo.admin.infrastructure.genre.models;

import com.fullcycle.catalogo.admin.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@JacksonTest
public class UpdateGenreRequestTest {

    @Autowired
    private JacksonTester<UpdateGenreRequest> json;

    @Test
    public void testUnmarshall() throws Exception {
        final var expectedName = "Ação";
        final var expectedCategory = "1234";
        final var expectedIsActive = true;

        final var json = """
        {
          "name": "%s",
          "categories_id": ["%s"],
          "is_active": %s
        }
        """.formatted(expectedName, expectedCategory, expectedIsActive);

        final var actualJson = this.json.parse(json);

        assertThat(actualJson)
            .hasFieldOrPropertyWithValue("name", expectedName)
            .hasFieldOrPropertyWithValue("categories", List.of(expectedCategory))
            .hasFieldOrPropertyWithValue("active", expectedIsActive);
    }
}
