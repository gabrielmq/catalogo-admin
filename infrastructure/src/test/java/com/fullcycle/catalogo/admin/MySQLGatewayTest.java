package com.fullcycle.catalogo.admin;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Inherited
@DataJpaTest
@Target(TYPE)
@Retention(RUNTIME)
@Tag("integrationTest")
@ActiveProfiles("test-integration")
@ExtendWith(MySQLCleanUpExtension.class)
@ComponentScan(
    basePackages = "com.fullcycle.catalogo.admin",
    useDefaultFilters = false,
    includeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*MySQLGateway")
    }
)
public @interface MySQLGatewayTest {
}
