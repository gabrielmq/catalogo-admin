package com.fullcycle.catalogo.admin;

import com.fullcycle.catalogo.admin.infrastructure.configuration.WebServerConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Inherited
@Target(TYPE)
@Retention(RUNTIME)
@AutoConfigureMockMvc
@ActiveProfiles("test-e2e")
@ExtendWith(MySQLCleanUpExtension.class)
@SpringBootTest(classes = WebServerConfiguration.class)
public @interface E2ETest {
}
