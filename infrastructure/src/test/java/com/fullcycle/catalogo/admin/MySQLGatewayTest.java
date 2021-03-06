package com.fullcycle.catalogo.admin;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@DataJpaTest
@ActiveProfiles("test-integration")
@ExtendWith(MySQLCleanUpExtension.class)
@ComponentScan(
    basePackages = "com.fullcycle.catalogo.admin",
    includeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".[MySQLGateway]")
    }
)
public @interface MySQLGatewayTest {
}
