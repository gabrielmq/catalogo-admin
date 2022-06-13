package com.fullcycle.catalogo.admin;

import com.fullcycle.catalogo.admin.infrastructure.configuration.WebServerConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Inherited
@ActiveProfiles("test-e2e")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(MySQLCleanUpExtension.class)
@SpringBootTest(classes = WebServerConfiguration.class)
public @interface E2ETest {
}
