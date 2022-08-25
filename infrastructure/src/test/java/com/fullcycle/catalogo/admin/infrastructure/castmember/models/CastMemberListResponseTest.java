package com.fullcycle.catalogo.admin.infrastructure.castmember.models;

import com.fullcycle.catalogo.admin.Fixture;
import com.fullcycle.catalogo.admin.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JacksonTest
class CastMemberListResponseTest {

    @Autowired
    private JacksonTester<CastMemberListResponse> json;

    @Test
    public void testMarshall() throws Exception {
        final var expectedId = "123";
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var expectedCreatedAt = Instant.now();

        final var response = new CastMemberListResponse(
            expectedId,
            expectedName,
            expectedType,
            expectedCreatedAt
        );

        final var actualJson = this.json.write(response);

        assertThat(actualJson)
            .hasJsonPathValue("$.id", expectedId)
            .hasJsonPathValue("$.name", expectedName)
            .hasJsonPathValue("$.type", expectedType.name())
            .hasJsonPathValue("$.created_at", expectedCreatedAt.toString());
    }

}