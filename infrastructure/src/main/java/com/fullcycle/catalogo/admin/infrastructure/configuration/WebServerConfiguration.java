package com.fullcycle.catalogo.admin.infrastructure.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ComponentScan("com.fullcycle.catalogo.admin")
public class WebServerConfiguration {
}
