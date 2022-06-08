package com.fullcycle.catalogo.admin;

import com.fullcycle.catalogo.admin.infrastructure.configuration.WebServerConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Inherited
@ActiveProfiles("test")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(CleanUpExtension.class)
@SpringBootTest(classes = WebServerConfiguration.class)
public @interface IntegrationTest {
}
