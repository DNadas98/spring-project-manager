package com.codecool.tasx.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "file:./dev.env", ignoreResourceNotFound = true)
public class PropertyConfig {
}
