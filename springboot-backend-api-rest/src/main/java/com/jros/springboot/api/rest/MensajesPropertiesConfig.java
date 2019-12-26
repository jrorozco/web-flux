package com.jros.springboot.api.rest;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
	@PropertySource("classpath:mensajes.properties")
})
public class MensajesPropertiesConfig {

}
