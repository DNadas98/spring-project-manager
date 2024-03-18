package net.dnadas.monolith.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Profile({"dev"})
@Configuration
@PropertySource(value = "file:./monolith.env")
public class DevPropertyConfig {
}
