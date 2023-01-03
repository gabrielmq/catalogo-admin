package com.fullcycle.catalogo.admin;

import com.fullcycle.catalogo.admin.infrastructure.configuration.WebServerConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Inherited
@ActiveProfiles("test-integration")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = WebServerConfiguration.class)
public @interface AMQPTest {
}
